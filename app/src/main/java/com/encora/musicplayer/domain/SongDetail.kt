package com.encora.musicplayer.domain

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

/**
 * data classes for xml response parsing
 * */
@Xml(name = "feed")
data class SongFeed(
    @PropertyElement(name = "id") val id: String?,
    @PropertyElement(name = "title") val title: String?,
    @Element(name = "author") val author: Author?,
    @Element(name = "entry") val songEntryList: List<SongEntry>?
)

@Xml(name = "author")
data class Author(
    @PropertyElement(name = "name") val name: String?,
    @PropertyElement(name = "uri") val uri: String?
)

@Xml(name = "entry", writeNamespaces = ["im=itunes.apple.com/rss"])
data class SongEntry(
    @PropertyElement(name = "id ") val id: String?,
    @PropertyElement(
        name = "title",
        converter = HtmlEscapeStringConverter::class
    ) val title: String?,
    @PropertyElement(name = "im:image") val imageUrl: String?,
    @PropertyElement(name = "im:artist", converter = HtmlEscapeStringConverter::class ) val artist: String?,
    @PropertyElement(name = "im:price") val price: String?,
    @PropertyElement(
        name = "content",
        converter = HtmlEscapeStringConverter::class)
    val content: String,
    @Element val link: Link,
    @Element val category: Category
)

@Xml(name = "category")
data class Category(
    @Attribute(name = "label") val label: String?
)

@Xml(name = "link", writeNamespaces = ["im=itunes.apple.com/rss"])
data class Link(
    @Attribute(name = "type") val type: String?,
    @Attribute(name = "href") val audioLink: String?
)