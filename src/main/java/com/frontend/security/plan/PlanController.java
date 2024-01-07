package com.frontend.security.plan;

import com.frontend.security.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PlanController {

    private final PlanService planService;

    @GetMapping("/plans")
    public ResponseEntity<Object> getPlans() {

        Collection<Plan> result;

        result = planService.getPlans();

        if(!result.isEmpty()){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else{
            return new ResponseEntity<>("No Plans Available", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<Object> getSinglePlan(@PathVariable("id") Integer id){

        Plan result = planService.getPlan(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<Object> updatePlan(@PathVariable("id") Integer id ,@Valid @RequestBody Plan plan) {

        Plan result = planService.updatePlan(id, plan).orElseThrow(() -> new ResourceNotFoundException(id));

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Object> deletePlan(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(planService.deletePlan(id), HttpStatus.OK);

    }
    @PostMapping("/plans")
    public ResponseEntity<Object> createPlan(@Valid @RequestBody Plan plan) {
        return new ResponseEntity<>(planService.createPlan(plan), HttpStatus.CREATED);
    }
}
