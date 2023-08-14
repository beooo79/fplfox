package com.github.beooo79;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;

public class AppTest
{

    @Test
    public void app_should_start()
    {
       assertThatNoException().isThrownBy(()-> FoxMain.main(new String[]{}));
    }
}
