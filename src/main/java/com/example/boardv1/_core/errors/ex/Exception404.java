package com.example.boardv1._core.errors.ex;

// 자원을 찾을 수 없다.
public class Exception404 extends RuntimeException{

    public Exception404(String message) {
        super(message);
    }
    
}