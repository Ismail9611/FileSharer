package com.iscool.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shared_files")
public class SharedFile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fileName")
    private String filename;

    @Column(name = "filePath")
    private String filepath;

    @Column(name = "fileUrl")
    private String url;

    @Column(name = "userFrom")
    private String from;

    @Column(name = "toEmail")
    private String to;

    @Column(name = "activeTo")
    private Date activeTo;

    public SharedFile(String filename, String filepath, String url, String from, String to, Date activeTo){
        this.filename = filename;
        this.filepath = filepath;
        this.url = url;
        this.from = from;
        this.to = to;
        this.activeTo = activeTo;
    }


}
