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
    val givenInput = Input.parseFrom(decodedString)

    lambdaLogger.log(givenInput.toString)

    val responseProto = givenInput.operator match {
      case "+" => Response((givenInput.op1 + givenInput.op2).toString)
      case "-" => Response((givenInput.op1 - givenInput.op2).toString)
      case "*" => Response((givenInput.op1 * givenInput.op2).toString)
      case "/" => Response((givenInput.op1 / givenInput.op2).toString)
      case _ => Response("Invalid operator passed : Expected syntax : x operand y")
    }

    lambdaLogger.log("Returning response....")

    lambdaLogger.log(responseProto.toString)
    val response = new APIGatewayProxyResponseEvent
    response.setBody(Base64.getEncoder.encodeToString(responseProto.toByteArray))

    response.setIsBase64Encoded(true)
    response.setHeaders(Map("Content-Type" -> "application/grpc+proto").asJava)

    return response

  }
}
