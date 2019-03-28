package joe.gallerydemo.util




fun main(){
    test()

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


fun test(){

//    if (type=="aa"){
//        return{x,y:Int,Int->
//            print(x+y)
//        }
//    }


    var filteredList = listOf(3,5,20,100,-25).filter(
        fun(el):Boolean{
            return Math.abs(el)>20
        })


    print(filteredList.toString())

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