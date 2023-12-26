package com.alibou.security.plan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public Plan createPlan(Plan plan){
        return planRepository.save(plan);
    }

    public Optional<Plan> findPlan(Integer id){
        return planRepository.findById(id);
    }


    public Collection<Plan> getPlans(){
        return planRepository.findAll();
    }

    public Optional<Plan> updatePlan(Integer id, Plan plan){
        Optional<Plan> result = planRepository.findById(id);

        try{
            Plan temp = result.get();
            temp.setName(plan.getName());
            temp.setDescription(plan.getDescription());
            temp.setImageURL(plan.getImageURL());
            temp.setPrice(plan.getPrice());
            temp.setPromo(plan.getPromo());
            return Optional.of(planRepository.save(temp));
        }catch(Exception e){
            return result;
        }
    }

    public Optional<Plan> deletePlan(Integer id){
        Optional<Plan> result = planRepository.findById(id);
        if(result.isPresent()){
            planRepository.deleteById(id);
        }
        return result;
    }


    public Optional<Plan> getPlan(Integer id){
        return planRepository.findById(id);
    }

}
