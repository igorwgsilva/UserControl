/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Erko
 */
public class PerfilAdministrador implements IPerfilUsuario {
     @Override
    public String getNomeExibicao() {
        return "Usuario administrador";
    }

    @Override
    public boolean isAdministrador() {
        return true;
    }
}
