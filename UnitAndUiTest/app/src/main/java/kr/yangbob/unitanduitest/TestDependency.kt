package kr.yangbob.unitanduitest

class TestDependency {
    private var field: Int = 0
    var field2: Int = 0

    fun isTest(): Boolean = false

    fun getRandomString(): String = "TestTest"

    fun setField(field: Int) {
        this.field = field
    }
}