package com.test.newsapp.network


import android.content.Context
import android.os.Build
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.test.newsapp.BuildConfig
import com.test.newsapp.NewsApp
import okhttp3.Headers
import okhttp3.Response
import java.io.File
import java.io.IOException


class Net(
    private val url: String,
    private val method: NetMethod = NetMethod.GET,
    private val body: Any? = null,
    queryParam: MutableMap<String, Any> = mutableMapOf(),
    pathParam: MutableMap<String, Any> = mutableMapOf(),
    headers: MutableMap<String, String> = mutableMapOf()
) {
    private var queryParam: MutableMap<String, Any>? = null
    private var pathParam: MutableMap<String, Any>? = null
    var files: MutableMap<String, File>? = HashMap()
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var progressListener: Service.Success<Float>? = null
    var tryAgain: (() -> Unit)? = null
    var excludeToken = false
    var okHttpResponse: Response? = null
    private val headersForRequest: MutableMap<String, String>
        get() {

            if (headers.isNullOrEmpty()) {
                headers = mutableMapOf()
            }

            if (TOKEN == null) {
                TOKEN = getToken()
            }

            if (TOKEN != null && !headers.containsKey("Authorization") && !excludeToken) {
                headers["Authorization"] = TOKEN!!
            }

            if (!headers.containsKey("Content-Type")) {
                headers["Content-Type"] = "application/json"
            }

            return headers
        }

    enum class NetMethod {

        GET, POST, DELETE, PUT, UPLOAD

    }

    object URL {
        var DOMAIN = "https://app.digitalinvea.com/api"
        var SERVER = "$DOMAIN/"
         var HOT_NEWS= SERVER+""
         var REGISTER= SERVER+""
         var LOGIN= SERVER+""
         var SEARCH_NEWS= SERVER+""



    }

    init {
        this.queryParam = queryParam
        this.pathParam = pathParam
        this.headers = headers
    }

    fun setProgressListener(progressListner: Service.Success<Float>): Net {
        this.progressListener = progressListner
        return this
    }

    fun setFiles(files: MutableMap<String, File>): Net {
        this.files = files
        return this
    }


    fun perform(
        success: ((result: String, okHttpResponse: Response?) -> Unit)?,
        error: ((error: NetException) -> Unit)?
    ): Net {
        var request: ANRequest<*>? = null

        when (method) {
            NetMethod.GET -> request = GET()
            NetMethod.POST -> request = POST()
            NetMethod.DELETE -> request = DELETE()
            NetMethod.PUT -> request = PUT()
            NetMethod.UPLOAD -> request = MULTIPART()
        }

        Log.i(
            TAG, "[" + method + "] url - " + url + ", body " + body + ", query " + queryParam +
                    ", path " + pathParam + ", headers " + headers + ", files " + files
        )

        request.getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {

            override fun onResponse(okHttpResponse: Response, response: String) {
                Log.i(TAG, "onResponse: " + okHttpResponse.networkResponse()?.request()?.url())
                this@Net.okHttpResponse = okHttpResponse
                if (okHttpResponse.isSuccessful) {
                    Log.i(TAG, "[$method] success - 200, body - $response")
                    headerParse(okHttpResponse.headers())
                    success?.let { it(response, okHttpResponse) }
                    return
                }
                //call back error
                var body: String? = null
                try {
                    body = okHttpResponse.networkResponse()!!.body()!!.string()
                } catch (e: IOException) {
                    e.printStackTrace()
                    error?.let { it(NetException(e.message, "BODY_CANT_PASS", -999)) }
                }

                Log.e(
                    TAG,
                    "[" + method + "] error - " + okHttpResponse.networkResponse()!!
                        .code() + ", body - " + body
                )
                error?.let {
                    it(NetException.init(body, okHttpResponse.networkResponse()!!.code()).apply {
                        tryAgain = { perform(success, error) }
                    })
                }

            }

            override fun onError(anError: ANError) {
                Log.i(TAG, "onError: " + anError.errorCode)
                Log.i(TAG, "onError: " + anError.errorBody)
                Log.i(TAG, "onError: " + anError.errorDetail)

                var exception: NetException? = null
                var body: String? = null
                try {
                    body = anError.errorBody
                    if (body != null)
                        exception = NetException.init(body, anError.errorCode)
                    else
                        exception = NetException("UNKNOWN", "BODY_CANT_PASS", anError.errorCode)

                } catch (e: Exception) {
                    e.printStackTrace()
                    exception = NetException(body, "BODY_CANT_PASS", -999)
                }

                Log.e(
                    TAG, "[" + method + "] error - " + exception!!.code +
                            ", body - " + exception.message +
                            ", description - " + anError.localizedMessage
                )
//                error?.error(exception.setTryAgain { perform(success, error) })
                error?.let {
                    it(exception.apply {
                        tryAgain = { perform(success, error) }
                    })
                }
            }
        })

        return this
    }

    private fun GET(): ANRequest<*> {
        val requestBuilder = AndroidNetworking.get(url)
        if (queryParam != null) requestBuilder.addQueryParameter(getAsString(queryParam!!))
        if (pathParam != null) requestBuilder.addPathParameter(getAsString(pathParam!!))
        requestBuilder.addHeaders(headersForRequest)
        return requestBuilder.build()
    }

    private fun POST(): ANRequest<*> {
        val requestBuilder = AndroidNetworking.post(url)
        if (queryParam != null) requestBuilder.addQueryParameter(getAsString(queryParam!!))
        if (pathParam != null) requestBuilder.addPathParameter(getAsString(pathParam!!))
        requestBuilder.addHeaders(headersForRequest)
        if (body != null) requestBuilder.addApplicationJsonBody(body)
        return requestBuilder.build()
    }

    private fun MULTIPART(): ANRequest<*> {
        val requestBuilder = AndroidNetworking.upload(url)
        if (queryParam != null) requestBuilder.addQueryParameter(getAsString(queryParam!!))
        if (pathParam != null) requestBuilder.addPathParameter(getAsString(pathParam!!))
        requestBuilder.addHeaders(headersForRequest)
        requestBuilder.addMultipartParameter(body)
        if (files != null) requestBuilder.addMultipartFile(files)

        return requestBuilder.build().setUploadProgressListener { bytesUploaded, totalBytes ->
            if (progressListener != null) {
                progressListener!!.success(bytesUploaded / totalBytes * 100f)
            }
        }
    }

    private fun DELETE(): ANRequest<*> {
        val requestBuilder = AndroidNetworking.delete(url)
        if (queryParam != null) requestBuilder.addQueryParameter(getAsString(queryParam!!))
        if (pathParam != null) requestBuilder.addPathParameter(getAsString(pathParam!!))
        requestBuilder.addHeaders(headersForRequest)
        if (body != null) requestBuilder.addApplicationJsonBody(body)
        return requestBuilder.build()
    }

    private fun PUT(): ANRequest<*> {
        val requestBuilder = AndroidNetworking.put(url)
        if (queryParam != null) requestBuilder.addQueryParameter(getAsString(queryParam!!))
        if (pathParam != null) requestBuilder.addPathParameter(getAsString(pathParam!!))
        requestBuilder.addHeaders(headersForRequest)
        if (body != null) requestBuilder.addApplicationJsonBody(body)
        return requestBuilder.build()
    }

    private fun headerParse(headers: Headers) {
        if (headers.get("Authorization") != null) {
            TOKEN = headers.get("Authorization")
            saveToken(TOKEN)
            Log.i(TAG, "token set - " + TOKEN!!)
        }
    }


    private fun getAsString(oldParams: Map<String, Any>): Map<String, String> {
        val param = HashMap<String, String>()
        for ((key, value) in oldParams) {
            param[key] = value.toString()
        }
        return param
    }



    private fun saveToken(token: String?) {
        val sharedPreferences = NewsApp.appContext
            .getSharedPreferences(
                PACKAGE,
                Context.MODE_PRIVATE
            )
        val er = sharedPreferences.edit()
        er.putString("TOKEN", token)
        er.apply()
    }

    private fun getToken(): String? {
        val sharedPreferences = NewsApp.appContext.getSharedPreferences(
            PACKAGE, Context.MODE_PRIVATE
        )
        return sharedPreferences.getString("TOKEN", null)
    }

    companion object {
        private val TAG = "Net"
        var TOKEN: String? = null
        fun getToken() = TOKEN
        const val PACKAGE = BuildConfig.APPLICATION_ID
    }


}


