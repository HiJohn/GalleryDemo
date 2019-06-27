package joe.gallerydemo.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.abs


fun main(){
//    test()
    coroutinesTest()

//    println(3.triple())

//    var result = {base:Int,exponent:Int->
//        var result = 1
//        for (i in 1..exponent){
//            result*=base
//        }
//        result
//    }(4,3)
//
//    print(result)
}


fun coroutinesTest(){
    GlobalScope.launch {
        delay(1000)
        println(" coroutines !")
    }
    println(" hello ,")
    runBlocking {
        delay(2000)
    }
}

fun test(){

//    if (type=="aa"){
//        return{x,y:Int,Int->
//            print(x+y)
//        }
//    }

    var texts = "adfasdfad"

    texts.let {

    }

    texts.apply {

    }

    texts.run {

    }


    var filteredList = listOf(3,5,20,100,-25).filter(
        fun(el):Boolean{
            return abs(el) >20
        })


    println(filteredList.toString())

}


fun Int.triple(): Int {
    return this * 3
}

enum class Color(val color:Int){
    RED(0XFF0000),
    GREEN(0X00FF00),
    BLUE(0X0000FF)
}



class Utils {


    fun getMathFunc(type:String):(Int)->Int{
        when(type){
            "square"->return {n:Int ->
                n*n
            }

            "cube" -> return {n:Int ->
                n*n*n
            }

            else ->return {
                n:Int ->
                var result = 1
                for (index in 2..n){
                    result*=index
                }
                result
            }
        }
    }

}