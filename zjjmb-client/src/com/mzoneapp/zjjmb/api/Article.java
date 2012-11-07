package com.mzoneapp.zjjmb.api;

import android.os.Bundle;

public class Article extends Headline {

	public String content;
	public String[] images;

	public static Article fromBundleToArticle(Bundle bd) {
		if(null == bd) return null;
		Article article = new Article();
		article.id = bd.getString("id");
		article.title = bd.getString("title");
		article.author = bd.getString("author");
		article.issuedate = bd.getString("issuedate");
		article.content = bd.getString("content");
		article.images = bd.getStringArray("images");
		article.type = bd.getString("type");
		return article;
	}

	public static Bundle convertArticleToBundle(Article article) {
		if(null == article) return null;
		Bundle bd = new Bundle();
		bd.putString("id", article.id);
		bd.putString("title", article.title);
		bd.putString("author", article.author);
		bd.putString("issuedate", article.issuedate);
		bd.putString("content", article.content);
		bd.putString("type", article.type);
		bd.putStringArray("images", article.images);
		return bd;
	}
}
