package Assignment0;

import java.io.IOException;


public class Tokenizer implements ITokenizer {
    private final Scanner scanner;
    private Token currentToken;
    private String currentValue;
    private Lexeme currentLexeme;
    private TokenizerState systemState;

    public Tokenizer() {
        this.scanner = new Scanner();
        this.currentToken = Token.NULL;
        this.currentValue = "";
        this.currentLexeme = new Lexeme(null, Token.NULL);
        this.systemState = TokenizerState.idle;
    }

    @Override
    public void open(String fileName) throws IOException, TokenizerException {
        try {
            scanner.open(fileName);
            scanner.moveNext();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Lexeme current() {
        return currentLexeme;
    }

    @Override
    public void moveNext() throws IOException, TokenizerException {
        String lexemeValue = "";
        currentToken = Token.NULL;

        while (systemState != TokenizerState.done) {
            //Get current character
            char currentCharacter = scanner.current();

            //If current character is end of line, end while loop
            if (currentCharacter == Scanner.EOF) {
                setToken(TokenizerState.done, Token.EOF);
                continue;
            }

            if (Character.isWhitespace(currentCharacter)) {
                setSystemStateDoneIfNotIdle();
                continue;
            }

            //if current character is an alphabet letter change current token to IDENT and exit loop
            if (Character.isLetter(currentCharacter)) {
                setToken(TokenizerState.readingIdentity, Token.IDENT);
                setValue(currentCharacter);
                continue;
            }

            //if current character is a digit change current token to INT_LIT and exit loop
            if (Character.isDigit(currentCharacter)) {
                setToken(TokenizerState.readingInt, Token.INT_LIT);
                setValue(currentCharacter);
                continue;
            }

            setToken(TokenizerState.idle, opCharTokenMatcher(currentCharacter));

            if (currentToken == Token.NULL) {
                throw new TokenizerException("Current character is NULL");
            }

            setValue(currentCharacter);

            systemState = TokenizerState.done;

        }
        systemState = TokenizerState.idle;

        try {
            double currentValueAsDouble = Double.parseDouble(currentValue);
            currentLexeme = new Lexeme(currentValueAsDouble, currentToken);
        } catch (NumberFormatException e) {
            currentLexeme = new Lexeme(currentValue, currentToken);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            scanner.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /*Checks if argument character matches any of the Operation characters
    Returns the corresponding token for given Operation
    * */
    private Token opCharTokenMatcher(char character) {
        switch (character) {
            case '+':
                return Token.ADD_OP;
            case '-':
                return Token.SUB_OP;
            case '*':
                return Token.MULT_OP;
            case '/':
                return Token.DIV_OP;
            case '=':
                return Token.ASSIGN_OP;
            case '(':
                return Token.LEFT_PAREN;
            case ')':
                return Token.RIGHT_PAREN;
            case ';':
                return Token.SEMICOLON;
            case '{':
                return Token.LEFT_CURLY;
            case '}':
                return Token.RIGHT_CURLY;
            default:
                return Token.NULL;
        }
    }

    private void setValue(char currentChar) throws IOException {
        currentValue = "";
        currentValue += currentChar;
        scanner.moveNext();
    }

    private void setToken(TokenizerState state, Token token) {
        systemState = state;
        currentToken = token;
    }

    private void setSystemStateDoneIfNotIdle() throws IOException {
        if (systemState != TokenizerState.idle) {
            systemState = TokenizerState.done;
        } else {
            scanner.moveNext();
        }
    }
}
