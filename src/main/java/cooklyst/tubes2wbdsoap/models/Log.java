package cooklyst.tubes2wbdsoap.models;

import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name="id")
    private int id;

    @Lob
    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, length = 16, name = "ip_address")
    private String IPAddress;

    @Column(nullable = false, name = "endpoint")
    private String endpoint;

    @Column(nullable = false, name = "timestamp")
    private Date timestamp;
}

