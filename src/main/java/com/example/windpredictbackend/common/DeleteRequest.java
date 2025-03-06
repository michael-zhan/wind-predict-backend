package com.example.windpredictbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Michael
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

