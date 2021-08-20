package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SimpleArticleService implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleService.class.getSimpleName());

    private final ArticleGenerator articleGenerator;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int count, Store<Article> articleStore) {
        LOGGER.info("Генерация статей в количестве {}", count);
        var words = wordStore.findAll();
        List<SoftReference<Article>> softArticleList = new ArrayList<>();
        int paketArticlesForSave = 0;
        for (int i = 0; i <= count; i++) {
            LOGGER.info("Сгенерирована статья № {}", i);
            WeakReference<Article> weakArticle = new WeakReference<>(articleGenerator.generate(words));
            softArticleList.add(new SoftReference<>(weakArticle.get()));
            paketArticlesForSave++;
            if (paketArticlesForSave == 5_000) {
                LOGGER.info("Paket = {}, softArticleList size = {}", paketArticlesForSave, softArticleList.size());
                for (SoftReference<Article> sofArticle : softArticleList) {
                    articleStore.save(sofArticle.get());
                }
                softArticleList.clear();
                paketArticlesForSave = 0;
                LOGGER.info("Paket = {}, softArticleList size = {}", paketArticlesForSave, softArticleList.size());
            }
        }
    }
}
