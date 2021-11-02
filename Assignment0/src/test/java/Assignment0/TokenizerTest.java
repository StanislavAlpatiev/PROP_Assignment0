package Assignment0;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    Tokenizer tokenizer = new Tokenizer();

    @Test
    void openDoesNotThrowForValidFileTest() {
        assertDoesNotThrow(() -> {
            tokenizer.open("src\\main\\java\\Assignment0\\program1.txt");
        });
    }

    @Test
    void openDoesThrowForInvalidFileTest() {
        assertThrows(IOException.class, () -> {
            tokenizer.open("src\\main\\java\\Assignment0\\program3.txt");
        });
    }

    @Test
    void currentTest() throws IOException, TokenizerException {
        tokenizer.open("src\\main\\java\\Assignment0\\program1.txt");
        tokenizer.moveNext();
        tokenizer.close();
        assertEquals("IDENT a",tokenizer.current().toString());
    }

    @Test
    void moveNextTest() throws IOException, TokenizerException {
        tokenizer.open("src\\main\\java\\Assignment0\\program1.txt");
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.close();
        String lexemeString = new Lexeme("1.0", Token.INT_LIT).toString();
        assertEquals(lexemeString, tokenizer.current().toString());
    }

    @Test
    void moveNextTillEOFTest() throws IOException, TokenizerException {
        tokenizer.open("src\\main\\java\\Assignment0\\program1.txt");
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        tokenizer.moveNext();
        //First EOF
        tokenizer.moveNext();
        //Second EOF to test that the token does not change
        tokenizer.moveNext();
        tokenizer.close();
        String lexemeString = new Lexeme(";", Token.EOF).toString();
        assertEquals(lexemeString, tokenizer.current().toString());
    }

    @Test
    void closeTest() {
    }
}