package com.yusy.keykeeper.services.autofill

import android.app.assist.AssistStructure
import android.app.assist.AssistStructure.ViewNode
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.Field
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.Presentations
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.autofill.HintConstants
import com.yusy.keykeeper.data.AppDataContainer
import com.yusy.keykeeper.utils.decryptPasswd
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class KeyKeeperAutofillService: AutofillService() {
    
    private var parsedStructure = ParsedStructure()

    private val accountsRepository = AppDataContainer(this).accountsRepository
    
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        // 初始化
        parsedStructure = ParsedStructure()

        val fillContexts = request.fillContexts
        val structure = fillContexts.last().structure
        val packageName = structure.activityComponent.packageName

        parseStructure(structure)

        Log.i("AUTOFILL", packageName + "/" + parsedStructure.usernameId + "/" + parsedStructure.passwordId)

        val fillResponseBuilder = FillResponse.Builder()

        val accountListFlow = accountsRepository.getAccountStreamByAppUrl(packageName)

        GlobalScope.launch {
            val accountList = accountListFlow.firstOrNull()
            if (accountList != null) {
                accountList.forEachIndexed { index, account ->
                    Log.i("AUTOFILL", index.toString() + ": " + account.uid)

                    val presentation = RemoteViews(packageName, android.R.layout.simple_list_item_1)
                    // TODO: 文本本地化
                    presentation.setTextViewText(android.R.id.text1, "填充账号 " + account.uid + " 的密码")

                    fillResponseBuilder.addDataset(
                        Dataset.Builder()
                            .setField(
                                parsedStructure.passwordId!!,
                                Field.Builder()
                                    .setValue(
                                        AutofillValue.forText(
                                            decryptPasswd(account.encryptedPasswd, account.encryptFunc, account.decryptKey)
                                        )
                                    )
                                    .setPresentations(
                                        Presentations.Builder()
                                            .setMenuPresentation(presentation)
                                            .setDialogPresentation(presentation)
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                }

                val fillResponse = fillResponseBuilder.build()

                callback.onSuccess(fillResponse)
            }
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        //
    }

    private fun parseStructure(structure: AssistStructure) {
        structure.run {
            (0 until windowNodeCount).map {
                parseNode(getWindowNodeAt(it).rootViewNode)
            }
        }
    }

    private fun parseNode(viewNode: ViewNode) {
        if (!viewNode.autofillHints.isNullOrEmpty()) {
            viewNode.autofillHints!!.forEach { hint ->
//                AutofillHintKeyWords.usernameHintKeyWords.forEach {
//                    if (hint.contains(it)) {
//                        parsedStructure.usernameId = viewNode.autofillId!!
//                    }
//                }
                AutofillHintKeyWords.passwdHintKeyWords.forEach {
                    if (hint.contains(it)) {
                        parsedStructure.passwordId = viewNode.autofillId!!
                    }
                }
            }
        }

        val childNode = viewNode.run {
            (0 until childCount).map {
                getChildAt(it)
            }
        }

        // 递归搜索
        childNode.forEach {
            parseNode(it)
        }
    }
}

data class ParsedStructure(
    var usernameId: AutofillId? = null,
    var passwordId: AutofillId? = null
)

object AutofillHintKeyWords {
//    val usernameHintKeyWords = listOf(
//        HintConstants.AUTOFILL_HINT_USERNAME,
//        HintConstants.AUTOFILL_HINT_NEW_USERNAME,
//        HintConstants.AUTOFILL_HINT_PHONE_NUMBER,
//        HintConstants.AUTOFILL_HINT_PHONE_NUMBER_DEVICE,
//        HintConstants.AUTOFILL_HINT_EMAIL_ADDRESS,
//        "账号",
//        "手机号",
//        "邮箱",
//        "用户名",
//        "昵称",
//        "nickname",
//        "username",
//        "name",
//        "email",
//        "address"
//    )

    val passwdHintKeyWords = listOf(
        HintConstants.AUTOFILL_HINT_PASSWORD,
        "密码",
        "password",
        "passwd",
    )
}