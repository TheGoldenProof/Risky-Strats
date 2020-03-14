package application;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.*;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

public class Main extends SimpleApplication {
	
	float speed = 10;
	Node player = new Node("Player");
	
	AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {
			Spatial camNode = player.getChild("Camera Node");
			speed = camNode.getLocalTranslation().z;
			
			if (name.equals("Left")) {
				player.move(value*-(speed+10)/2, 0, 0);
			}
			
			if (name.equals("Right")) {
				player.move(value*(speed+10)/2, 0, 0);
			}
			
			if (name.equals("Up")) {
				player.move(0, value*(speed+10)/2, 0);
			}
			
			if (name.equals("Down")) {
				player.move(0, value*-(speed+10)/2, 0);
			}
		}
	};
	
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			Spatial camNode = player.getChild("Camera Node");
		     
			if (name.equals("Zoom In") && camNode.getLocalTranslation().z > 3) {
				camNode.move(0, 0, -1);
			}
			
			if (name.equals("Zoom Out")) {
				camNode.move(0, 0, 1);
			}
			
		}
	};
	
	public static void main(String[] args) {
		
		Main app = new Main();
		
		AppSettings settings = new AppSettings(false);
		
		settings.setTitle("Risky Strats");
		
		app.setSettings(settings);
		
		app.start();
	}

	@Override
	public void simpleInitApp() {
		initCamera();
		initInput();
		createSkybox();
		
		Quad s = new Quad(32, 32);
		Geometry g = new Geometry("Plane", s);
		Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture grass = assetManager.loadTexture("assets/textures/grass_1300.png");
		grass.setWrap(WrapMode.Repeat);
		s.scaleTextureCoordinates(new Vector2f(5, 5));
		m.setTexture("ColorMap", grass);
		g.setMaterial(m);
		g.setLocalTranslation(-16, -16, 0);
		rootNode.attachChild(g);
		
//		DirectionalLight sun = new DirectionalLight(new Vector3f(-0.5f, -0.5f, -0.5f).normalizeLocal(), ColorRGBA.White);
//		rootNode.addLight(sun);
	    
	}
	
	public void initCamera() {
		
		rootNode.attachChild(player);
		
		flyCam.setEnabled(false);

		CameraNode camNode = new CameraNode("Camera Node", cam);
		camNode.setControlDir(ControlDirection.SpatialToCamera);
		player.attachChild(camNode);
		camNode.setLocalTranslation(0, 0, speed);
		camNode.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
		
	}
	
	public void initInput() {
		
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W), new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S), new KeyTrigger(KeyInput.KEY_DOWN));
		
		inputManager.addMapping("Zoom In", new MouseAxisTrigger(MouseInput.AXIS_WHEEL,false));
		inputManager.addMapping("Zoom Out", new MouseAxisTrigger(MouseInput.AXIS_WHEEL,true));
		
		inputManager.addListener(analogListener, "Left", "Right", "Up", "Down");
		inputManager.addListener(actionListener, "Zoom In", "Zoom Out");
		
	}
	
	public void createSkybox() {
		
		Texture[] lagoon = new Texture[] {
				assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg"),
				assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg"),
				assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg"),
				assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg"),
				assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg"),
				assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg")
		};
		int i = 0;
		getRootNode().attachChild(SkyFactory.createSky(assetManager, lagoon[i++], lagoon[i++], lagoon[i++], lagoon[i++], lagoon[i++], lagoon[i++]));
		
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		
	}
}
