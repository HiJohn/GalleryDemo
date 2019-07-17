package joe.gallerydemo

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.abs

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun test2(){
        var n = 10
        for (i in 2..n){
            print("i是：$i ")
        }
        for (i in 2 until n){
            println("i value :$i")
        }
    }

    @Test
    fun testPerson(){
        var p = Person()
        var a = ""
        var b = p.let {
            a = it.name
            it.name = "aaaaa"
        }

        println("a:$a")
        println("name:${p.name}")
    }

    class Person{
        var name = "barry"
    }




    @Test
    fun test(){




//        var mood = "I am sad "
//        run{
//            val mood = "I am happy"
//            println(mood)
//        }
//        println(mood)


        val filteredList = listOf(3,5,20,100,-25).filter(
                fun(el):Boolean{
                    return abs(el) >20
                })


        print(filteredList.toString())
    }
}
