package cn.gionrose.facered

import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.handler.CorsHandler


/**
 * @description TODO
 * @author facered
 * @date 2023/11/18 23:48
 */
class Gionrose{

    val array = JsonArray ()
    init {
        val responseJson1 = JsonObject().put("name", "facered").put ("id", "2").put ("place", "china")
        val responseJson2 = JsonObject().put("name", "qiuhua").put ("id", "3").put ("place", "American")
        val responseJson3 =  JsonObject.of("name", "babala", "id", "4", "place", "flldz")
        array.add (responseJson1).add (responseJson2).add (responseJson3)
    }
    fun start() {
        val vertx = Vertx.vertx()
        val router = Router.router(vertx)

        val allowedHeaders: MutableSet<String> = HashSet()
        allowedHeaders.add("x-requested-with")
        allowedHeaders.add("Access-Control-Allow-Origin")
        allowedHeaders.add("origin")
        allowedHeaders.add("Content-Type")
        allowedHeaders.add("accept")
        allowedHeaders.add("X-PINGARUNER")

        val allowedMethods: MutableSet<HttpMethod> = HashSet()
        allowedMethods.add(HttpMethod.GET)
        allowedMethods.add(HttpMethod.POST)
        allowedMethods.add(HttpMethod.OPTIONS)
        allowedMethods.add(HttpMethod.DELETE)
        allowedMethods.add(HttpMethod.PATCH)
        allowedMethods.add(HttpMethod.PUT)

        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods))


        router.route("/list").handler { context ->

            context.response().putHeader("Content-Length", array.encode().length.toString())
            context.response().putHeader("Content-Type", "application/json")
            context.response().write (array.encode())

        }

        router.route("/delete/:id").handler {context ->
            val params = context.request().params()
            val id = params["id"]
            val temp = JsonArray ().addAll(array)
            temp.forEach {
                val target = (it as JsonObject).getString("id")
                if (target == id)
                {
                    array.remove (it)
                    context.response().putHeader("Content-Length", "delete_success".length.toString())
                    context.response().putHeader("Content-Type", "text/plain")
                    context.response ().write("delete_success")
                }
            }

        }

        router.route ("/ws").handler {context ->
            context.request ()
                .toWebSocket()
                .onSuccess { webSocket ->
                    webSocket.writeTextMessage("printAge")

                    webSocket.textMessageHandler { message ->
                        println ("客户端: 接收到客户端的消息 $message")
                    }

                    webSocket.closeHandler { println ("服务器: this webSocket is close") }
                }
                .onFailure(Throwable::printStackTrace)

        }

        vertx.createHttpServer()
            .requestHandler (router)
            .listen(1906)
            .onSuccess {server ->
                println("this server run on ${server.actualPort()}")
            }.onFailure (Throwable::printStackTrace)

    }
}

fun main() {
    val verticle = Gionrose()
    verticle.start()
    Ws ().run()
}

class Ws
{
//    fun run ()
//    {
//        val vertx = Vertx.vertx()
//        vertx.createHttpClient().webSocketAbs ("ws://localhost:1906/ws", HeadersMultiMap.headers(), WebsocketVersion.V00, listOf())
//            .onSuccess { webSocket ->
//                webSocket.textMessageHandler { message ->
//                    if (message.lowercase() == "printname")
//                    {
//                        val result = printName()
//                        if (result)
//                            webSocket.writeTextMessage("success")
//                        else
//                            webSocket.writeTextMessage("failed")
//                    }
//                    if (message.lowercase() == "printage")
//                    {
//                        val result =  printAge()
//                        if (result)
//                            webSocket.writeTextMessage("success")
//                        else
//                            webSocket.writeTextMessage("failed")
//                    }
//
//                }
//            }
//            .onFailure(Throwable::printStackTrace)
//    }
    fun run ()
    {
        val vertx = Vertx.vertx()
        val webClient = WebClient.create(vertx)

//        webClient.get (1906, "localhost", "/delete/2")
//            .`as`(BodyCodec.string())
//            .send()
//            .onSuccess {
//                println (it.body())
//            }
//            .onFailure(Throwable::printStackTrace)
//
//        webClient.post(1906, "localhost", "/list")
//            .`as` (BodyCodec.jsonArray())
//            .send()
//            .onSuccess {
//                val body = it.body()
//                println(body.encode())
//            }
//            .onFailure(Throwable::printStackTrace)
    }
}

fun printName (): Boolean
{
    println ("facered")
    return true
}
fun printAge (): Boolean
{
    println (21)
    return true
}