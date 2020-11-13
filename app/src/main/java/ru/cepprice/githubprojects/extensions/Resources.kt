package ru.cepprice.githubprojects.extensions

import android.content.res.Resources
import android.text.Html
import android.text.Spanned
import androidx.annotation.StringRes

fun Resources.getHtmlSpannedString(
    @StringRes id: Int,
    vararg formatArgs: Any
): Spanned = Html.fromHtml(getString(id, *formatArgs))
