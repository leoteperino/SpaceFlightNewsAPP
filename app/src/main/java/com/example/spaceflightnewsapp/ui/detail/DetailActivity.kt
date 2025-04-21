package com.example.spaceflightnewsapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Toolbar
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.title = "Detalle del Artículo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarDetail.setNavigationOnClickListener { finish() }

        // Obtener el artículo
        val article = intent.getSerializableExtra("article") as? Article
        if (article == null) {
            finish()
            return
        }

        // Cargar imagen
        binding.imageView.load(article.image_url) {
            crossfade(true)
            error(R.drawable.image_error)
            fallback(R.drawable.image_error)
        }

        // Setear textos
        binding.tvTitle.text = article.title ?: ""
        binding.tvSummary.text = article.summary ?: ""
        binding.tvNewsSite.text = article.news_site ?: ""
        binding.tvPublishedAt.text = article.published_at ?: ""

        val url = article.url ?: ""
        val spannable = SpannableString(url)

        val customClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                widget.context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@DetailActivity, R.color.link_blue_strong)
                ds.isUnderlineText = true
            }
        }

        spannable.setSpan(
            customClickableSpan,
            0,
            url.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvUrl.text = spannable
        binding.tvUrl.movementMethod = LinkMovementMethod.getInstance()
    }
}