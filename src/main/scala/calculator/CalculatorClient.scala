//package calculator
//
//import io.grpc.calculator.Input
//import scalaj.http.{Base64, Http}
//
//
//
//
//object CalculatorClient extends App {
//  //  Http("https://a4r9djxhra.execute-api.us-east-1.amazonaws.com/default/FirstLambda").
//
//  val data = Input(10, 20, "+")
//  val result = Http("https://a4r9djxhra.execute-api.us-east-1.amazonaws.com/default/CalculatorLambda").postData(data.toByteArray)
//    .header("Content-Type", "application/grpc+proto")
//    .header("x-api-key", "QDqkEgvLZZ2rqt5RlZ6Gx2SpOm0FsFRE5yjsByQZ").asString
//
//  val body = result.body
//
//  println(Base64.decode(body).toString)
//
//  println(result.body)
//
//}
