package com.epam.logistic.reader;

import com.epam.logistic.exception.ReaderException;
import lombok.extern.log4j.Log4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Path.*;
import java.util.stream.Stream;

@Log4j
public class DataFileReader {
    private static final String PATH = "src/main/resources/data";

    public String readShopFromFile(String path) throws ReaderException {
        Stream<String> fileData;
        try {
            try {
                fileData = (Files.lines(Path.of(path), StandardCharsets.UTF_8));
            } catch (FileNotFoundException | NoSuchFileException e){
                fileData = (Files.lines(Path.of(PATH), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new ReaderException(e);
        }
        StringBuilder sb = new StringBuilder();
        fileData.forEach(sb::append);
        return String.valueOf(sb);
    }
}
