package template.generation.controller;
import template.generation.model.Personne;
import template.generation.repository.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/personnes")
public class PersonneController  
{
    private  PersonneRepository personneRepository;
    
    @Autowired
    public PersonneController(PersonneRepository personneRepository){
        
        this.personneRepository = personneRepository;
    
    }

    @GetMapping
    public ResponseEntity<List<Personne>> findAll()
    {
        
            try {
                List<Personne> liste = personneRepository.findAll();
                if (!liste.isEmpty()) {
                    return ResponseEntity.ok().body(liste);
                } else {
                    return ResponseEntity.status(404).body(liste);
                }
            }catch (Exception e) {
               return ResponseEntity.status(500).body(null);
            }
    
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Personne> findOne(@PathVariable Integer id)
    {
        
        try{
            Personne one = personneRepository.findById(id).orElse(null);
            if(one != null){
                return ResponseEntity.ok().body(one);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(one);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    
    }

    @PostMapping
    public ResponseEntity<Personne> insert(@RequestBody Personne personne)
    {
        
        try{
            Personne creation = personneRepository.save(personne);
            if(creation != null){
               return ResponseEntity.status(HttpStatus.CREATED).body(creation);
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(creation);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    
    }

    @PutMapping("/{id}")
    public ResponseEntity<Personne> update(@PathVariable Integer id, @RequestBody Personne personne)
    {
        
        try{
            Optional<Personne> opt = personneRepository.findById(id);
            if(opt.isPresent()){
                Personne update = opt.get();
                personne.setId(update.getId());
                personneRepository.save(personne);
                if(personne != null){
                    return ResponseEntity.ok().body(personne);
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body( personne);
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body( personne);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id)
    {
        
        personneRepository.deleteById(id);
    
    }

    

    

}

