package template.generation.model;
import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Etudiant 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nom_etudiant;
	private String prenom_etudiant;
	private double note;
	private Date date_naissance;
	private int age;

	public void setId( int id )
	{
		this.id=id; 
	}

	public void setNom_etudiant( String nom_etudiant )
	{
		this.nom_etudiant=nom_etudiant; 
	}

	public void setPrenom_etudiant( String prenom_etudiant )
	{
		this.prenom_etudiant=prenom_etudiant; 
	}

	public void setNote( double note )
	{
		this.note=note; 
	}

	public void setDate_naissance( Date date_naissance )
	{
		this.date_naissance=date_naissance; 
	}

	public void setAge( int age )
	{
		this.age=age; 
	}


	public int getId()
	{
		return this.id; 
	}

	public String getNom_etudiant()
	{
		return this.nom_etudiant; 
	}

	public String getPrenom_etudiant()
	{
		return this.prenom_etudiant; 
	}

	public double getNote()
	{
		return this.note; 
	}

	public Date getDate_naissance()
	{
		return this.date_naissance; 
	}

	public int getAge()
	{
		return this.age; 
	}


	public Etudiant(int id, String nom_etudiant, String prenom_etudiant, double note, Date date_naissance, int age )
	{
		this.setId(id); 
		this.setNom_etudiant(nom_etudiant); 
		this.setPrenom_etudiant(prenom_etudiant); 
		this.setNote(note); 
		this.setDate_naissance(date_naissance); 
		this.setAge(age); 
	}
	public Etudiant()
	{

	}
}

