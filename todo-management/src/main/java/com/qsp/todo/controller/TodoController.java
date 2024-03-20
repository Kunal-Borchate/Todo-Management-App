package com.qsp.todo.controller;

import com.qsp.todo.dto.TodoDto;
import com.qsp.todo.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TodoDto> addTodo(@RequestBody TodoDto todoDto){
        TodoDto savedTodo=todoService.addTodo(todoDto);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("{id}")
    public ResponseEntity<TodoDto> getTodo(@PathVariable Long id){
        TodoDto todoDto=todoService.getTodo(id);
        return new ResponseEntity<>(todoDto,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos(){
        List<TodoDto>todoDto=todoService.getAllTodos();
//        return new ResponseEntity<>(todoDto,HttpStatus.OK);
        return ResponseEntity.ok(todoDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto,@PathVariable Long id){
        TodoDto updatedTodo=todoService.updateTodo(todoDto,id);
        return ResponseEntity.ok(updatedTodo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id){
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Todo deleted successfully!");
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("{id}/complete")
    public ResponseEntity<TodoDto> completeTodo(@PathVariable Long id){
        TodoDto todo=todoService.completeTodo(id);
        return ResponseEntity.ok(todo);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("{id}/incomplete")
    public ResponseEntity<TodoDto>incompleteTodo(@PathVariable Long id){
        TodoDto todo=todoService.incompleteTodo(id);
        return ResponseEntity.ok(todo);
    }
}
