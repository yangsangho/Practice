package kr.yangbob.unitanduitest

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MockitoTest {
    @Mock
    private lateinit var testDependency: TestDependency

    @Before
    fun before(){
        `when`(testDependency.isTest()).thenReturn(true)
        `when`(testDependency.setField(anyInt())).then {
            `when`(testDependency.isTest()).thenReturn(false)
        }
        `when`(testDependency.getRandomString()).thenReturn("changeStr")
    }

    @Test
    fun addition_isCorrect() {
        assertThat(testDependency.isTest(), `is`(true))
        testDependency.setField(100)
        assertThat(testDependency.isTest(), `is`(false))
        assertThat(testDependency.getRandomString(), `is`("changeStr"))
    }
}