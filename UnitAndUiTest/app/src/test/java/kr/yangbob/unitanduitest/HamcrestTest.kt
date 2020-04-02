package kr.yangbob.unitanduitest

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.`object`.IsCompatibleType
import org.junit.Test
import java.math.BigDecimal

class HamcrestTest {

    /* <Core>
    * 1. anything
    * 2. describeAs
    * 3. is */
    @Test
    fun basicTest() {
        // actual과 동일하면 pass
        assertThat(false, `is`(false))
        assertThat(2, `is`(2))
        assertThat("aa", `is`("aa"))
    }

    @Test
    fun anythingTest() {
        // 어떤 타입이든 pass
        assertThat("aa", anything())
        assertThat(2, anything())
    }

    @Test
    fun describeAsTest() {
        // fail이면 description 출력
        assertThat(2, describedAs("실패메세지닷!", `is`(2)))
    }

    /* <Object>
    * 1. equalTo
    * 2. hasToString
    * 3. instanceOf
    * 4. typeCompatibleWith
    * 5. sameInstance
    * 6. nullValue, notNullValue  */
    @Test
    fun nullTest() {
        assertThat(null, nullValue())
    }

    @Test
    fun notNullTest() {
        assertThat("aa", notNullValue())
    }

    @Test
    fun hasToTest() {
        // auctual의 toString 값과 동일하면 pass
        assertThat(2, hasToString("2"))
    }

    @Test
    fun equalToTest() {
        // is와 차이점을 모르겠음
        assertThat(false, equalTo(false))
        assertThat(2, equalTo(2))
        assertThat("aa", equalTo("aa"))
    }

    @Test(expected = AssertionError::class)
    fun instanceOfTest() {
        assertThat(2, instanceOf(String::class.java))
    }

    @Test
    fun isCompatibleTest() {
        // 특이하게 얘는 생성자로 하네 바로.
        assertThat(ChildClass()::class.java, IsCompatibleType(ParentClass::class.java))

        // typeCompatibleWith() 를 써도 사용법이나 결과 동일
        assertThat(Integer::class.java, typeCompatibleWith(Number::class.java))
        assertThat(ParentClass::class.java, typeCompatibleWith(ParentClass::class.java))
    }

    @Test
    fun sameInstanceTest() {
        val child = ChildClass()
        val parent = ParentClass()

//        assertThat(child, sameInstance(parent)) // Error : 실행도 안됨 (이유를 정확히 못 밝힘)
//        assertThat(parent, sameInstance(child)) // Error : 실행도 안됨 (이유를 정확히 못 밝힘)
        assertThat(child, sameInstance(child))
        assertThat(parent, sameInstance(parent))
    }

    /* <Logical>
    * 1. allOf
    * 2. anyOf
    * 3. not */
    @Test
    fun allOfTest() {
        assertThat(2, allOf(
                `is`(2),
                instanceOf(Int::class.java),
                notNullValue()
        ))
    }

    @Test
    fun anyOfTest() {
        assertThat(2, anyOf(
                `is`(7777),
                instanceOf(Int::class.java),
                nullValue()
        ))
    }

    @Test
    fun notTest() {
        // 얘는 여러 개 X
        assertThat(2, not(`is`(7777)))
    }

    /* <Numbers>
    * 1. closeTo
    * 2. greaterThan, greaterThanOrEqualTo, lessThan, lessThanOrEqualTo */
    @Test
    fun closeToTest() {
        // closeTo(기준값, 플마오차값)
        val value = 7
        value.toBigDecimal()
        assertThat(7.0, closeTo(10.0, 4.0))
        assertThat(value.toBigDecimal(), closeTo(10.toBigDecimal(), BigDecimal(4)))
    }

    @Test
    fun compareNumTest() {
        val value = 7
        assertThat(value, greaterThan(6))
        assertThat(value, greaterThanOrEqualTo(7))
        assertThat(value, lessThan(8))
        assertThat(value, lessThanOrEqualTo(7))
    }

    /* <Beans>
    * 1. hasProperty */
    @Test
    fun propertyTest() {
        // public property만 확인 가능
        val testDependency = TestDependency()
        assertThat(testDependency, hasProperty("field2"))
        assertThat(testDependency, hasProperty("field2", `is`(0)))
        testDependency.field2 = 7
        assertThat(testDependency, hasProperty("field2", `is`(7)))
    }

    /* <Collections>
    * 1. array
    * 2. hasEntry, hasKey, hasValue
    * 3. hasItem, hasItems,
    * 4. hasSize : collection 사이즈 확인
    * 4. hasItemInArray */
    @Test
    fun arrayTest() {
        // matcher들 배열 만들어주는 함수. 함수간 이동같은 거 할 때나 필요할 듯? [IsArray<T> 타입]
        // 아니면 matcher 배열을 변수로 선언해놓을 때 등
        anyOf(array(`is`(2), `is`(7)))
    }

    @Test
    fun mapContainingTest() {
        val map = mapOf("two" to 2, "four" to 4, "six" to 6)
        assertThat(map, hasEntry("two", 2))
        assertThat(map, hasEntry(not(isEmptyOrNullString()), greaterThan(5)))
        assertThat(map, hasKey("two"))
        assertThat(map, hasKey(not(isEmptyOrNullString())))
        assertThat(map, hasValue(2))
        assertThat(map, hasValue(`is`(4)))
    }

    @Test
    fun collectionContainingTest() {
        val list = listOf(2, 4, 6, 8, 10)
        assertThat(list, hasItem(2))
        assertThat(list, hasItem(greaterThan(9)))
        assertThat(list, hasItems(2, 4))
        assertThat(list, hasItems(lessThan(4), greaterThan(7)))
        assertThat(list, hasSize(5))
    }

    @Test
    fun hasItemInArrayTest(){
        // collection이 아니고, 그냥 array일 때 사용
        val list = arrayOf("foo", "bar")
        assertThat(list, hasItemInArray(startsWith("ba")))
        assertThat(list, hasItemInArray("bar"))
    }

    /* <Text>
    * 1. equalToIgnoringCase
    * 2. equalToIgnoringWhiteSpace
    * 3. containsString, endsWith, startsWith */
    @Test
    fun equalToIgnoringCaseTest(){
        val text = "yangbob"
        assertThat(text, equalToIgnoringCase("YANGBOB"))
    }

    @Test
    fun equalToIgnoringWhiteSpaceTest(){
        val text = "    yangbob     "
        assertThat(text, equalToIgnoringWhiteSpace("      YANGBOB               "))
    }

    @Test
    fun stringContainingTest(){
        val text = "yangbob"
        assertThat(text, containsString("yan"))
        assertThat(text, startsWith("ya"))
        assertThat(text, endsWith("ob"))
    }
}

