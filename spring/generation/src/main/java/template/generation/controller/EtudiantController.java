package template.generation.controller;
import template.generation.model.Etudiant;
import template.generation.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/etudiants")
public class EtudiantController  
{
    private  EtudiantRepository etudiantRepository;
    
    @Autowired
    public EtudiantController(EtudiantRepository etudiantRepository){
        
        this.etudiantRepository = etudiantRepository;
    
    }

    @GetMapping
    public ResponseEntity<List<Etudiant>> findAll()
    {
        
            try {
                List<Etudiant> liste = etudiantRepository.findAll();
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
    public ResponseEntity<Etudiant> findOne(@PathVariable Integer id)
    {
        
        try{
            Etudiant one = etudiantRepository.findById(id).orElse(null);
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
    public ResponseEntity<Etudiant> insert(@RequestBody Etudiant etudiant)
    {
        
        try{
            Etudiant creation = etudiantRepository.save(etudiant);
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
    public ResponseEntity<Etudiant> update(@PathVariable Integer id, @RequestBody Etudiant etudiant)
    {
        
        try{
            Optional<Etudiant> opt = etudiantRepository.findById(id);
            if(opt.isPresent()){
                Etudiant update = opt.get();
                etudiant.setId(update.getId());
                etudiantRepository.save(etudiant);
                if(etudiant != null){
                    return ResponseEntity.ok().body(etudiant);
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body( etudiant);
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body( etudiant);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id)
    {
        
        etudiantRepository.deleteById(id);
    
    }

    

    

}

