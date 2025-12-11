/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Erko
 */
public class PerfilComum implements IPerfilUsuario {
    @Override
    public String getNomeExibicao() {
        return "Usuario comum";
    }

    @Override
    public boolean isAdministrador() {
        return false;
    }
    
}
