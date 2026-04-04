package com.splitmoney.service

import com.splitmoney.model.GroupEntity
import com.splitmoney.model.data.Balance
import com.splitmoney.model.data.CreateGroupInput
import com.splitmoney.model.data.Settlement
import com.splitmoney.repository.GroupRepository
import com.splitmoney.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.math.min


@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createGroup(input: CreateGroupInput): GroupEntity {
        val members = userRepository.findAllById(input.membersId)
        if(members.size != input.membersId.size){
            throw IllegalArgumentException("One or more members do not match")
        }
        val adminUser = userRepository.findById(input.createdBy)
            .orElseThrow { IllegalArgumentException("User not found") }
        val newGroup = GroupEntity(
            name = input.name,
            memberIds = input.membersId.toHashSet(),
            createdBy = adminUser
        )

        return groupRepository.save(newGroup)

    }
    @Transactional(readOnly = true)
    fun getGroupById(groupId: UUID): GroupEntity {
        return groupRepository.findById(groupId)
            .orElseThrow{ NoSuchElementException("Group not found with id $groupId") }
    }

    @Transactional(readOnly = true)
    fun calculateBalance(groupId: UUID): List<Balance>{
        val group = groupRepository.findById(groupId).
        orElseThrow { NoSuchElementException("Group not found with id $groupId") }
        val expenses = group.expenses
        val membersId = group.memberIds
        if(expenses.isEmpty() || membersId.isEmpty() ) return emptyList()

        val users = userRepository.findAllById(membersId).associateBy { it.id }
        val totalAmount = expenses.sumOf { it.amount.toDouble() }
        val equalShare = totalAmount / membersId.size

        val netBalances = membersId.associateWith {
            memberId ->
            val paidAmount = expenses.filter {
                it.payer.id == memberId
            }.sumOf { it.amount.toDouble() }
            paidAmount - equalShare
        }
        val creditors = netBalances.filter { it.value > 0 }.toMutableMap()
        val debtors = netBalances.filter { it.value < 0 }.toMutableMap()

        val allSettlement = mutableListOf<Settlement>()

        for((debtorId, debtAmt) in debtors) {
            var remainingDebt = debtAmt

            for((creditorId, creditAmt) in creditors) {
                if(creditAmt < 0.0) continue
                val settlementAmount = min(remainingDebt, creditAmt)

                if(settlementAmount > 0){
                    allSettlement.add(
                        Settlement(
                            from = users[debtorId]!!,
                            to = users[creditorId]!!,
                            amount = settlementAmount
                        )
                    )
                    remainingDebt -= settlementAmount
                    creditors[creditorId] = creditAmt - settlementAmount
                    if(remainingDebt <= 0.0) break
                }


            }
        }
        return membersId.map { memberId ->
            Balance(
                user = users[memberId]!!,
                amount = netBalances[memberId] ?: 0.0,
                settlements = allSettlement.filter { it.from.id == memberId }
                )
        }
    }
}