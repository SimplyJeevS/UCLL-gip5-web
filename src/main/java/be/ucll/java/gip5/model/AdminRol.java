package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="adminRol", schema = "gip5")
public class AdminRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="adminRolId")
    private Long adminRolId;

    private AdminRol(AdminRolBuilder builder){

    }
    public AdminRol(){

    }

    public static final class AdminRolBuilder{
        private Long id;
        private Long adminRolId;
        private AdminRolBuilder(){}
        public static AdminRolBuilder AdminRol(){
            return new AdminRolBuilder();
        }
        public AdminRolBuilder(AdminRol copy){
            this.id = copy.id;
            this.adminRolId = copy.adminRolId;
        }
        public AdminRolBuilder id(Long id){
            this.id = id;
            return this;
        }
        public AdminRolBuilder adminRolId(Long adminRolId){
            this.adminRolId = adminRolId;
            return this;
        }
        public AdminRol build(){
            return new AdminRol(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminRolId() {
        return adminRolId;
    }

    public void setAdminRolId(Long adminRolId) {
        this.adminRolId = adminRolId;
    }
}
