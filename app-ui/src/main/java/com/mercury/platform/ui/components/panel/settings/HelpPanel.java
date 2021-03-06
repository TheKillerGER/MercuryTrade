package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.impl.NotesFrame;
import com.mercury.platform.ui.frame.impl.SettingsFrame;
import com.mercury.platform.ui.frame.impl.test.TestCasesFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by Константин on 26.02.2017.
 */
public class HelpPanel extends JPanel implements HasUI {
    private ComponentsFactory componentsFactory;
    public HelpPanel() {
        super();
        componentsFactory = new ComponentsFactory();
        this.setBackground(AppThemeColor.TRANSPARENT);
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton openTutorial = componentsFactory.getBorderedButton("Open tutorial");
        openTutorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
                    FramesManager.INSTANCE.preShowFrame(NotesFrame.class);
                }
            }
        });
        JButton openTests = componentsFactory.getBorderedButton("Open tests");
        openTests.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
                    FramesManager.INSTANCE.preShowFrame(TestCasesFrame.class);
                }
            }
        });
        JButton patchNotes = componentsFactory.getBorderedButton("Open patch notes");
        patchNotes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    NotesLoader notesLoader = new NotesLoader();
                    java.util.List<Note> patchNotes = notesLoader.getPatchNotes();
                    if(patchNotes.size() != 0){
                        NotesFrame patchNotesFrame = new NotesFrame(patchNotes,NotesFrame.NotesType.PATCH);
                        patchNotesFrame.init();
                        patchNotesFrame.showComponent();
                    }
                }
            }
        });
        this.add(openTutorial);
        this.add(openTests);
        this.add(patchNotes);
    }
}
