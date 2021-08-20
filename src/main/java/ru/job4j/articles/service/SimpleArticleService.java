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
        LOGGER.info("Генерация статей в количестве {}", count);
        var words = wordStore.findAll();
        int paket = 0;
        for (int i = 0; i < count; i++) {
            LOGGER.info("Сгенерирована статья № {}", i);
            WeakReference<Article> articleWeak = new WeakReference<>(articleGenerator.generate(words));
            List<SoftReference<Article>> softReferenceList = new ArrayList<>();
            softReferenceList.add(new SoftReference<>(articleWeak.get()));
            paket++;
            if (paket == 100_000) {
                for (SoftReference<Article> article : softReferenceList) {
                    articleStore.save(article.get());
                }
                softReferenceList.clear();
                paket = 0;
            }
        }
        //var articles =
/*                IntStream.iterate(0, i -> i < count, i -> i + 1)
                .peek(i -> LOGGER.info("Сгенерирована статья № {}", i))
                .mapToObj((x) -> new WeakReference(articleGenerator.generate(words)))
                .filter((x) -> x.get() != null)
                .map(articleStore::save);*/
                //.map((x) -> articleStore.save((Article) x.get()));
                //.collect(Collectors.toList()); //.forEach(articleStore.save(WeakReference::get));
/*        for (WeakReference article : articleWeak) {
            if (article != null) {
                articleStore.save((Article) article.get());
            }
        }*/

        //articleStore.sa
        //articles.forEach(WeakReference::get).;

        //articles.forEach(articleStore::save);
    }
}
