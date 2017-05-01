/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utilisateurs.gestionnaires.GestionnaireUtilisateurs;
import utilisateurs.modeles.Utilisateur;

/**
 *
 * @author jux
 */
@WebServlet(name = "ServletUsers", urlPatterns = {"/ServletUsers"})
public class ServletUsers extends HttpServlet {
    @EJB
    private GestionnaireUtilisateurs gestionnaireUtilisateurs;


    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods. 
     * @param request servlet request 
     * @param response servlet response 
     * @throws ServletException if a servlet-specific error occurs 
     * @throws IOException if an I/O error occurs 
     */  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        // Pratique pour décider de l'action à faire  
        String action = request.getParameter("action");  
        String forwardTo = "";  
        String message = "";
        
        
  
        if (action != null) {
              if (action.equals("connexion")) {
                 
                  Collection<Utilisateur> user = gestionnaireUtilisateurs
                    .getOneUserByLoginAndLastName(
                        //récupération des paramètres de la requête
                        request.getParameter("login_connexion"),
                        request.getParameter("lastname_connexion")); 
                
                
                request.setAttribute("listeDesUsers", user); 
                
                System.out.println("user:"+user.isEmpty());
                
                if(!user.isEmpty()) {
                    HttpSession session = request.getSession(true);
                     message = "Connecté.";
                } else {
                    message = "Votre login et mot de passe n'existe pas...";
                }
                
                //soumettre les paramètres de la requête à la couche service et récupération du résultat
                    //Utilisateur user = new Utilisateur(login, pwd);
                    forwardTo = "index.jsp?action=connexion=envoyer"; 
                    //réponse à l'utilisateur
                    
                
               // RequestDispatcher dispatcher = request.getRequestDispatcher("resultatLogin.jsp");
               // dispatcher.forward(request, response);
                    
                 
                
                
            } else if (action.equals("listerLesUtilisateurs")) {
                
                Collection<Utilisateur> liste = gestionnaireUtilisateurs.getAllUsers();  
                request.setAttribute("listeDesUsers", liste);  
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));
                request.setAttribute("paginationPages", gestionnaireUtilisateurs.getPaginationInfos((Long)request.getAttribute("numberOfUsers")));
                //forwardTo = "index.jsp?action=listerLesUtilisateurs"; 
                forwardTo = "index.jsp?action=listerLesUtilisateurs&moreNext=yes&morePrevious=no";
                message = "Liste des utilisateurs"; 
                
            } else if (action.equals("creerUtilisateursDeTest")) {  
                
                gestionnaireUtilisateurs.creerUtilisateursDeTest();  
                Collection<Utilisateur> liste = gestionnaireUtilisateurs.getAllUsers();  
                request.setAttribute("listeDesUsers", liste); 
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));
                forwardTo = "index.jsp?action=listerLesUtilisateurs";  
                message = "Création des utilisateurs de test";  
                
            } else if (action.equals("creerUnUtilisateur")) {
                gestionnaireUtilisateurs.creeUtilisateur(request.getParameter("nom"), request.getParameter("prenom"), request.getParameter("login"));
                Collection<Utilisateur> liste = gestionnaireUtilisateurs.getAllUsers();  
                request.setAttribute("listeDesUsers", liste);  
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));
                forwardTo = "index.jsp?action=listerLesUtilisateurs";  
                message = "Création de l'utilisateur "+request.getParameter("login");
                
            } else if (action.equals("chercherParLogin")) {     
                Collection<Utilisateur> user = gestionnaireUtilisateurs.getOneUserByLogin(request.getParameter("login")); 
                request.setAttribute("listeDesUsers", user);  
                forwardTo = "index.jsp?action=listerLesUtilisateurs";  
                message = "Utilisateur avec le login "+request.getParameter("login");
                
            } else if (action.equals("updateUtilisateur")) {   
                gestionnaireUtilisateurs.updateUtilisateur(request.getParameter("nom"),request.getParameter("prenom"),request.getParameter("login")); 
                Collection<Utilisateur> liste = gestionnaireUtilisateurs.getAllUsers();
                request.setAttribute("listeDesUsers", liste);
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));
                forwardTo = "index.jsp?action=listerLesUtilisateurs";  
                message = "Modification de l'utilisateur "+request.getParameter("login");
                
            } else if (action.equals("deleteUtilisateur")) {     
                gestionnaireUtilisateurs.deleteUser(request.getParameter("login")); 
                Collection<Utilisateur> liste = gestionnaireUtilisateurs.getAllUsers();
                request.setAttribute("listeDesUsers", liste); 
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));
                forwardTo = "index.jsp?action=listerLesUtilisateurs";  
                message = "Suppression de l'utilisateur "+request.getParameter("login");
                   
            } else if (action.equals("getUsersPaginated")) {
          
                Collection<Utilisateur> liste = gestionnaireUtilisateurs.getUsersPaginated(3,7);  
                request.setAttribute("listeDesUsers", liste);  
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));
                request.setAttribute("paginationPages", gestionnaireUtilisateurs.getPaginationInfos((Long)request.getAttribute("numberOfUsers")));
                forwardTo = "index.jsp?action=listerLesUtilisateurs&moreNext=yes&morePrevious=no";
                message = "Liste des utilisateurs";
                
            } else if (action.equals("nextResult")) {  
                Collection<Utilisateur> liste = (Collection<Utilisateur>) gestionnaireUtilisateurs.getNextUsersPaginated(); 
                if(liste == null)
                {
                   forwardTo = "index.jsp?action=listerLesUtilisateurs&moreNext=no&morePrevious=yes"; 
                }
                else
                {
                   forwardTo = "index.jsp?action=listerLesUtilisateurs&moreNext=yes&morePrevious=yes"; 
                }
                //forwardTo = "index.jsp?action=listerLesUtilisateurs";
                request.setAttribute("listeDesUsers", liste); 
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));;
                message = "Liste des utilisateurs"; 
                
            } else if (action.equals("previousResult")) {  
                Collection<Utilisateur> liste = gestionnaireUtilisateurs.getPreviousUsersPaginated();  
                if(liste == null)
                {
                   forwardTo = "index.jsp?action=listerLesUtilisateurs&moreNext=yes&morePrevious=no"; 
                }
                else
                {
                   forwardTo = "index.jsp?action=listerLesUtilisateurs&moreNext=yes&morePrevious=yes"; 
                }
                //forwardTo = "index.jsp?action=listerLesUtilisateurs";
                request.setAttribute("listeDesUsers", liste); 
                request.setAttribute("numberOfUsers", gestionnaireUtilisateurs.getNumberOfUsers().get(0));
                request.setAttribute("numberOfPages", gestionnaireUtilisateurs.getNumberOfUsers().get(1));
                message = "Liste des utilisateurs";
                
            } else {  
                forwardTo = "index.jsp?action=todo";  
                message = "La fonctionnalité pour le paramètre " + action + " est à implémenter !";  
            }  
            
        }  
  
        RequestDispatcher dp = request.getRequestDispatcher(forwardTo + "&message=" + message);  
        dp.forward(request, response);  
        // Après un forward, plus rien ne peut être exécuté après !  
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
