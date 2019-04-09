package by.bsuir.masters.iit.builder;

import by.bsuir.masters.iit.model.Node;
import by.bsuir.masters.iit.model.TfIdfInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SearchProcessor {

    public Map<String, Double> findRelevantDocs(String query, TfIdfInfo tfIdfInfo) {
        Map<String, Long> wordCounts = getWords(query)
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Double> queryTfIdfs = wordCounts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> tfIdfInfo.getIdfs().getOrDefault(entry.getKey(), 0d)));

        Map<String, Double> fileRelevancyMap = new HashMap<>();

        queryTfIdfs.forEach((word, queryTfIdf) -> tfIdfInfo.getTermFilesTfIdfDictionary()
            .getOrDefault(word, Collections.emptyMap()).forEach((filename, value) -> {
                if (!fileRelevancyMap.containsKey(filename)) {
                    List<Double> fileTfIdfs = queryTfIdfs.keySet().stream().map(
                        entry -> tfIdfInfo.getTermFilesTfIdfDictionary()
                            .getOrDefault(entry, Collections.emptyMap()).getOrDefault(filename, 0d))
                        .collect(Collectors.toList());

                    fileRelevancyMap
                        .put(filename, computeCosineSimilarity(
                            new ArrayList<>(queryTfIdfs.values()), fileTfIdfs));
                }
            }));
        return fileRelevancyMap.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private Double computeCosineSimilarity(List<Double> queryTfIdfs, List<Double> fileTfIfs) {
        if (queryTfIdfs.size() != fileTfIfs.size()) {
            throw new RuntimeException("TfIdfs data sizes do not match");
        }
        double dotProduct = 0;
        double queryNorm = 0;
        double fileNorm = 0;
        for (int i = 0; i < queryTfIdfs.size(); i++) {
            dotProduct += queryTfIdfs.get(i) * fileTfIfs.get(i);
            queryNorm += Math.pow(queryTfIdfs.get(i), 2);
            fileNorm += Math.pow(fileTfIfs.get(i), 2);
        }

        queryNorm = Math.sqrt(queryNorm);
        fileNorm = Math.sqrt(fileNorm);
        if (queryNorm * fileNorm == 0) {
            return 0d;
        } else {
            return dotProduct / (queryNorm * fileNorm);
        }
    }

    public TfIdfInfo buildTermFileDictionary(Map<String, Node> docMap) {
        Map<String, Map<String, Long>> wordCountsForFiles = buildWordCountsForFiles(docMap);
        Map<String, Map<String, Double>> termDictionary = new HashMap<>();
        Map<String, Double> idfMap = new HashMap<>();

        wordCountsForFiles.forEach((filename, wordCounts) ->
                wordCounts.keySet().forEach(word -> {
                    Double idf = idfMap.computeIfAbsent(word, w -> getInverseDocumentFrequency(wordCountsForFiles, w));
                    Double tfIdf = getTermFrequency(wordCounts, word) * idf;
                    termDictionary.computeIfAbsent(word, w -> new HashMap<>()).put(filename, tfIdf);
                })
        );

        return new TfIdfInfo(idfMap, termDictionary);
    }

    private Map<String, Map<String, Long>> buildWordCountsForFiles(Map<String, Node> docMap) {
        return docMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> buildWordCounts(entry.getValue())));
    }

    private Double getTermFrequency(Map<String, Long> wordCounts, String term) {
        return ((double) Optional.ofNullable(wordCounts.get(term)).orElse(0L) / wordCounts.keySet().size());
    }

    private Double getInverseDocumentFrequency(Map<String, Map<String, Long>> wordCountsForFiles, String term) {
        long docsCountWithTerm = wordCountsForFiles.entrySet()
                .stream()
                .filter(entry -> entry.getValue().containsKey(term))
                .count();
        if (docsCountWithTerm == 0) {
            return 0d;
        }
        return Math.log((double) wordCountsForFiles.keySet().size() / docsCountWithTerm);
    }

    private Map<String, Long> buildWordCounts(Node node) {
        Map<String, Long> terms = new HashMap<>();
        addChildrenToWordCounts(node.getChildren(), terms);
        return terms;
    }

    private void addChildrenToWordCounts(List<Node> children, Map<String, Long> targetTermMap) {
        children.forEach(node -> {
            if (node.getType() == null && node.getValue() != null) {
                String value = node.getValue();
                List<String> words = getWords(value);
                words.forEach(word -> targetTermMap.put(word,
                        Optional.ofNullable(targetTermMap.get(word)).map(count -> count + 1).orElse(1L)));
            } else {
                addChildrenToWordCounts(node.getChildren(), targetTermMap);
            }
        });
    }

    private List<String> getWords(String value) {
        return Arrays.stream(value.split("\\s+"))
                .map(token -> token.replaceAll("[^\\w]", ""))
                .collect(Collectors.toList());
    }
}
