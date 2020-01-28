package com.yangbob.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
        webView.loadUrl("https://google.com")

        registerForContextMenu(webView)

        etUrl.setOnEditorActionListener { _, actionId, _ ->
            if( actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                webView.loadUrl(etUrl.text.toString())
                true
            }
            else false
        }
    }

    override fun onBackPressed() {
        if( webView.canGoBack() ) webView.goBack()
        else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("https://google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("http://www.naver.com")
                return true
            }
            R.id.action_daum -> {
                webView.loadUrl("http://www.daum.net")
                return true
            }
            R.id.action_call -> {
                // 전화걸기
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:010-6476-7883")
                }
                if( intent.resolveActivity(packageManager) != null)
                {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                // 문자 보내기
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("sms:010-6476-7783")
                    putExtra("sms_body", "테스트 문자메세지 입니다.")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_email -> {
                // 이메일 보내기
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("did0528@naver.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "테스트 메일 제목")
                    putExtra(Intent.EXTRA_TEXT, "테스트 메일 본문....!!")
                }
//                val chooser = Intent.createChooser(intent, "Email Choose")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu( menu: ContextMenu?, v: View?,
                          menuInfo: ContextMenu.ContextMenuInfo? ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_share -> {
                // 문자열 공유
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "테스트 문자열입니당.")
                }
                val chooser = Intent.createChooser(intent, "text share Choose")
                if( intent.resolveActivity(packageManager) != null)
                {
                    startActivity(chooser)
                }
                return true
            }
            R.id.action_browser -> {
                // 기본 웹 브라우저에서 열기
                val intent = Intent(Intent.ACTION_VIEW).apply{
                    data = Uri.parse("http://daum.net")
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
