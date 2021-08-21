package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.lang.ref.WeakReference;

public class SimpleArticleService implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleService.class.getSimpleName());

    private final ArticleGenerator articleGenerator;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int count, Store<Article> articleStore) {
        LOGGER.info("Генерация статей в количестве {}", count);
        var words = wordStore.findAll(); // загружаем слова
        WeakReference<Article> weakReference; // weak ссылка на статью
        for (int i = 0; i < count; i++) {
            LOGGER.info("Сгенерирована статья № {}", i);
            weakReference = new WeakReference<>(articleGenerator.generate(words)); // генерируем статью
            articleStore.save(weakReference.get()); // сохраняем статью
        }
    }
}
