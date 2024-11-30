package com.dicoding.picodiploma.loginwithanimation.helper

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.di.Injection
import com.dicoding.picodiploma.loginwithanimation.data.local.entity.StoryEntity
import com.dicoding.picodiploma.loginwithanimation.view.widget.ImageBannerWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory, CoroutineScope {

    private val repository = Injection.provideRepository(mContext)
    private val parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var mWidgetItems = ArrayList<Bitmap>()


    override fun onCreate() {
        launch {
            repository.getSession().collect { session ->
                if (session.token != "") {
                    val stories = repository.getWidgetStories()
                    convertBitmap(stories)
                    updateWidgetItems(mWidgetItems)
                }
            }

        }
    }

    private fun convertBitmap(stories:List<StoryEntity>){
        stories.forEach { story ->
            Glide.with(mContext)
                .asBitmap()
                .load(story.photoUrl)
                .override(400, 400)
                .centerCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        mWidgetItems.add(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }

    private fun updateWidgetItems(newItems: ArrayList<Bitmap>) {
        mWidgetItems = newItems
        AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged(
            AppWidgetManager.getInstance(mContext).getAppWidgetIds(
                ComponentName(mContext, ImageBannerWidget::class.java)
            ), R.id.stack_view
        )
    }

    override fun onDataSetChanged() {

    }

    override fun onDestroy() {
        parentJob.cancel()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])


        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}