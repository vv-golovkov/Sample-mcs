package com.home.m1service.task;

import com.home.common.MyPersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/m1doc")
@Tag(name = "M1 Management", description = "API to control M1")
public class M1OpenApiController {

    @Operation(
            summary = "Отримати деталі користувача за ID",
            description = "Виконує пошук користувача в базі даних за його унікальним ідентифікатором.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Користувача успішно знайдено"),
                    @ApiResponse(responseCode = "404", description = "Користувача з таким ID не знайдено"),
                    @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера")
            }
    )
    @GetMapping
    public ResponseEntity<MyPersonDTO> get() {
        return ResponseEntity.ok(new MyPersonDTO("vg", new Random().nextInt(10, 100)));
    }

    @PostMapping
    public ResponseEntity<String> post(@RequestBody MyPersonDTO p) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Person created: %s".formatted(p));
    }
}
