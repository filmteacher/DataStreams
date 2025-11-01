import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/*
/ Comp Programming II - Lab09 - DataStreams
/ @author Matt Bennett
*/

public class SearchFrame extends JFrame
{
    JPanel mainPnl;
    JPanel titlePnl;
    JPanel controlsPnl;
    JPanel buttonsPnl;
    JPanel searchPnl;
    JPanel textPnl;

    JLabel title;

    JButton loadBtn;
    JButton quitBtn;

    JButton searchBtn;
    JTextField searchFld;

    JTextArea originalArea;
    JScrollPane originalScroller;
    JTextArea resultsArea;
    JScrollPane resultsScroller;

    File selectedFile;
    String rec = "";

    public SearchFrame()
    {
        createMainPnl();

        createTitlePnl();
        mainPnl.add(titlePnl);

        createControlsPnl();
        mainPnl.add(controlsPnl);

        createTextPnl();
        mainPnl.add(textPnl);

        add(mainPnl);

        setTitle("Comp Prog II - Lab09 - DataStreams");
        setSize(800, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createMainPnl()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new FlowLayout());
        mainPnl.setBackground(Color.LIGHT_GRAY);
    }

    private void createTitlePnl()
    {
        titlePnl = new JPanel();
        title = new JLabel("Search for a Phrase in a Text File");
        title.setFont(new Font("Verdana", Font.BOLD, 22));
        titlePnl.add(title);
        titlePnl.setBackground(Color.LIGHT_GRAY);
    }

    private void createControlsPnl()
    {
        controlsPnl = new JPanel();
        controlsPnl.setLayout(new GridLayout(1,2));
        controlsPnl.setBackground(Color.WHITE);
        controlsPnl.setBorder(new TitledBorder(new EtchedBorder(),"CONTROLS"));

        controlsPnl.setPreferredSize(new Dimension(680,80));

        buttonsPnl = new JPanel();
        buttonsPnl.setLayout(new GridLayout(1,2));
        buttonsPnl.setBackground(Color.WHITE);
        buttonsPnl.setBorder(new TitledBorder(new EtchedBorder(),"1. LOAD a file or QUIT the app:"));

        loadBtn = new JButton("LOAD a file");
        loadBtn.addActionListener((ActionEvent ae) ->
        {
            JFileChooser chooser = new JFileChooser();

            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                this.selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                try (Stream<String> lines = Files.lines(file)) {
                    lines.forEach(l -> originalArea.append(l + "\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        quitBtn = new JButton("QUIT the app");
        quitBtn.addActionListener((ActionEvent ae) ->
        {
            System.exit(0);
        });

        buttonsPnl.add(loadBtn);
        buttonsPnl.add(quitBtn);

        controlsPnl.add(buttonsPnl);

        searchPnl = new JPanel();
        searchPnl.setLayout(new GridLayout(1,2));
        searchPnl.setBackground(Color.WHITE);
        searchPnl.setBorder(new TitledBorder(new EtchedBorder(),"2. ENTER phrase and click SEARCH:"));


        searchFld = new JTextField();


        searchBtn = new JButton("Search");
        searchBtn.addActionListener((ActionEvent ae) ->
        {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Please load a file first!",
                        "No File Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String searchString = searchFld.getText();

            if (Objects.equals(searchString, "")) {
                JOptionPane.showMessageDialog(this, "Enter a search string first!",
                        "No Search String Entered", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Path file = selectedFile.toPath();

            try {
                Files.lines(file)
                        .filter(line -> line.contains(searchString))
                        .forEach(line -> resultsArea.append(line + "\n\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        searchPnl.add(searchFld);
        searchPnl.add(searchBtn);

        controlsPnl.add(searchPnl);
    }

    private void createTextPnl()
    {
        textPnl = new JPanel();
        textPnl.setLayout(new GridLayout(1,2));
        textPnl.setBackground(Color.WHITE);
        textPnl.setBorder(new TitledBorder("RESULTS"));

        originalArea = new JTextArea(25,25);
        originalArea.setFont(new Font("Verdana", Font.PLAIN, 14));
        originalArea.setEditable(false);
        originalArea.setLineWrap(true);
        originalArea.setWrapStyleWord(true);
        originalScroller = new JScrollPane(originalArea);
        originalScroller.setBorder(new TitledBorder(new EtchedBorder(),"Original:"));
        textPnl.add(originalScroller);

        resultsArea = new JTextArea(25,25);
        resultsArea.setFont(new Font("Verdana", Font.PLAIN, 14));
        resultsArea.setEditable(false);
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsScroller = new JScrollPane(resultsArea);
        resultsScroller.setBorder(new TitledBorder(new EtchedBorder(),"Search Results:"));
        textPnl.add(resultsScroller);
    }
}
