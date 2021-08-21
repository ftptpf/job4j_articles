package ru.job4j.articles.service.generator;

import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RandomArticleGenerator implements ArticleGenerator {
    @Override
    public Article generate(List<Word> words) {
        var wordsCopy = new ArrayList<>(words);
        Collections.shuffle(wordsCopy);
        // оборачиваем генерируемую статью в weak ссылку
        WeakReference<String> content = new WeakReference<>(wordsCopy.stream()
                .map(Word::getValue)
                .collect(Collectors.joining(" ")));
        wordsCopy = null; // обнуляем скопированный лист слов
        return new Article(content.get());
    }
}
