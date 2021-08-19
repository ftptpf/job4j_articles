package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.lang.ref.WeakReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleArticleService implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleService.class.getSimpleName());

    private final ArticleGenerator articleGenerator;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int count, Store<Article> articleStore) {
        LOGGER.info("Геренация статей в количестве {}", count);
        var words = wordStore.findAll();
/*        for (int i = 0; i < count; i++) {
            System.out.printf("Сгенерирована статья № %s", i);
            System.out.println();
            WeakReference<Article> articleWeak = new WeakReference<>(articleGenerator.generate(words));
            articleStore.save(articleWeak.get());
        }*/
        var articles = IntStream.iterate(0, i -> i < count, i -> i + 1)
                .peek(i -> LOGGER.info("Сгенерирована статья № {}", i))
                .mapToObj((x) -> new WeakReference(articleGenerator.generate(words)))
                .collect(Collectors.toList()); //.forEach(articleStore.save(WeakReference::get));
        for (WeakReference article : articles) {
            if (article != null) {
                articleStore.save((Article) article.get());
            }
        }

        //articleStore.sa
        //articles.forEach(WeakReference::get).;

        //articles.forEach(articleStore::save);
    }
}
