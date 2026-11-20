package com.chenyan.kotlindemo

import com.chenyan.kotlindemo.data.CallerIdRepository
import com.chenyan.kotlindemo.data.NumberInfo
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CallerIdRepositoryTest {
     @Test
     fun localChannelEmitsBeforeCloud() = runTest {
          val repo = CallerIdRepository()
          val results = mutableListOf<NumberInfo>()
          repo.identify("13800138000").toList(results)
          assertEquals(NumberInfo.Source.LOCAL, results.first().source)
          assertEquals(NumberInfo.Source.CLOUD, results.last().source)
     }
}