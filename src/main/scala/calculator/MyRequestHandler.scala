package calculator

import java.util.Base64

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import io.grpc.calculator.{Input, Response}
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import scala.collection.JavaConverters._
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger


class MyRequestHandler extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent] {


  override def handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent = {

    val lambdaLogger = context.getLogger

    lambdaLogger.log("Request received...")

    val decodedString = Base64.getDecoder.decode(request.getBody)
    val inputProto = Input.parseFrom(decodedString)

    lambdaLogger.log(inputProto.toString)

    val responseValue = inputProto.operator match {
      case "+" => (inputProto.op1 + inputProto.op2)
      case "-" => (inputProto.op1 - inputProto.op2)
      case "*" => (inputProto.op1 * inputProto.op2)
      case "/" => (inputProto.op1 / inputProto.op2)
      case _ => "Error : Format : op1 operator op2"
    }

    lambdaLogger.log("Returning response....")

    val responseProto = Response().update(
      _.inputExpression := inputProto.op1.toString + inputProto.operator.toString + inputProto.op2.toString,
      _.message := responseValue.toString
    )


    if (!responseValue.toString.contains("Error")) {
      responseProto.update(_.message := "Success",
        _.output := responseValue.toString.toDouble
      )
    }

    //    val responseProto = Response(inputProto.op2.toString + inputProto.operator + inputProto.op2, responseValue.toString.toDouble)
    lambdaLogger.log(responseProto.toString)
    val response = new APIGatewayProxyResponseEvent
    response.setBody(Base64.getEncoder.encodeToString(responseProto.toByteArray))

    response.setIsBase64Encoded(true)
    response.setHeaders(Map("Content-Type" -> "application/grpc+proto").asJava)

    return response

  }
}
