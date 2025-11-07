package liquidjava.api.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import liquidjava.errors.ErrorEmitter;

public class ErrorEmitterBasicTest {

    @Test
    public void estadoInicialSemErro() {
        ErrorEmitter er = new ErrorEmitter();
        assertFalse(er.foundError());
        assertEquals(0, er.getErrorStatus());
    }
}