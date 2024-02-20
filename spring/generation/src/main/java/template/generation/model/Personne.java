package template.generation.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Personne 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nom;
	private String prenom;

	public void setId( int id )
	{
		this.id=id; 
	}

	public void setNom( String nom )
	{
		this.nom=nom; 
	}

	public void setPrenom( String prenom )
	{
		this.prenom=prenom; 
	}


	public int getId()
	{
		return this.id; 
	}

	public String getNom()
	{
		return this.nom; 
	}

	public String getPrenom()
	{
		return this.prenom; 
	}


	public Personne(int id, String nom, String prenom )
	{
		this.setId(id); 
		this.setNom(nom); 
		this.setPrenom(prenom); 
	}
	public Personne()
	{

	}
}

