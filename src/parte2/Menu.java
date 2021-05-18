package parte2;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Menu extends JFrame {
	JLabel lblTexto;
	JComboBox<String> lstLista;

	public Menu() {
		super("Menu");

        //lock.releaseLock(0);
		this.setSize(500, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		String[] valores = { "1", "2", "3" };
		lstLista = new JComboBox<String>(valores);

		/*lstLista.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("has seleccionado: " + (String) lstLista.getSelectedItem());
				//lblTexto.setText((String) lstLista.getSelectedItem());
			}
		});*/
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			

				switch ((String) lstLista.getSelectedItem()) {
					case "1":
						System.out.println("Ha seleccionado: lista de usuarios");
						// Enviar mensaje MSG_LISTA_USUARIOS
						//foutc.writeObject(new Msg_lista_usuarios());
						//foutc.flush();
						//lock.takeLock(0); 
						// System.out.println("PRUEBA : Cliente - lista");
						//lock.releaseLock(0);
						break;
					case "2":
						System.out.println("Ha seleccionado: emitir fichero");
						String path = JOptionPane.showInputDialog("Escriba el nombre del fichero:");
						System.out.println("El fichero que quiere es: " + path);
						// (se deben permitir otras acciones y descargas simultaneas)
						// Enviar mensaje MSG_SOLICITUD_FICHERO
						//lock.takeLock(0); 
						// System.out.println("Indica el nombre del fichero que quieres descargar: ");
						// String nombre_fichero = teclado.nextLine();
						//lock.releaseLock(0);
	
						// foutc.writeObject(new Msg_solicitud_fichero(nombre_fichero,nombre_usuario));
						// foutc.flush();
						break;
					case "3":
						System.out.println("Ha seleccionado: salir");
						Menu.this.dispose();
						System.exit(0);
						// Enviar mensaje MSG_CERRAR_CONEXION
						// foutc.writeObject(new Msg_cerrar_conexion(nombre_usuario));
						// foutc.flush();
						// ok = false; //Cerramos este hilo y el hilo OS se queda esperando confirmación   
						break;
					default:
						//lock.takeLock(0); 
						System.out.println("Opcion no valida");
						//lock.releaseLock(0);
						break;
				}
			}
		});

		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Enviar mensaje MSG_CERRAR_CONEXION
				// foutc.writeObject(new Msg_cerrar_conexion(nombre_usuario));
				// foutc.flush();
				// ok = false; 
				Menu.this.dispose();
				System.exit(0);
			}
		});

		JPanel pnl = new JPanel();
		
		pnl.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		pnl.setLayout(new GridLayout(4, 2, 5, 5));

		pnl.add(new JLabel("1 - Consultar el nombre de los usuarios conectados y su informacion\n"));
		pnl.add(new JLabel(""));
		pnl.add(new JLabel("2 - Descargar informacion\n"));
		pnl.add(new JLabel(""));
		pnl.add(new JLabel("3 - Desconectarse del servidor\n"));
		pnl.add(lstLista);
		pnl.add(btnSalir);
		pnl.add(btnAceptar);
		this.getContentPane().add(pnl);
		this.pack();

	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Menu ej = new Menu();

			}
		});
	}

}
