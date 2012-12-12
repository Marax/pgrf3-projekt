import transforms.Vec3D;


public class Telesa {
	
	public static Vec3D compute(double x, double y, int i){
		
		if(i == 1){
			//Snake (k)
					double s = Math.PI * 0.5 - Math.PI * y;
					double t = 2 * Math.PI * x;
					
					return new Vec3D(
							2*(1-s)*(3+Math.cos(t))*Math.cos(2*Math.PI*s),
							4*(1-s)*(3+Math.cos(t))*Math.sin(2*Math.PI*s),
							10*s+(1-s)*Math.sin(t));
		} else
		if(i == 6){
			//kartez3 (k)
					double s = Math.PI * 0.5 - Math.PI * y;
					double t = 2 * Math.PI * x;
					
					return new Vec3D(
							Math.cos(s) *10,
							Math.sin(s)+Math.cos(t) *10,
							t*10);
		} else
		if(i == 2){
			//Sphe1 bumpy sphere (s)
					double t = Math.PI * 0.5 - Math.PI * y *2;
					double s = 2 * Math.PI * x *2;
					
					double r = 1+0.2*Math.sin(6*s)*Math.sin(5*t);
					r*= 10;
					
					return new Vec3D(
							Math.sin(t) * Math.cos(s) * r ,
							Math.sin(t) * Math.sin(s) * r , 
							Math.cos(s) * r );
		} else
		if(i == 5){
			//Sphe2  (s)
					double s = Math.PI * 0.5 - Math.PI * y *2;
					double t = 2 * Math.PI * x *2; 
					double r = 1+Math.sin(4*t);
					r *= 8; 
					
					return new Vec3D(
							Math.sin(t) * Math.cos(s) * r ,
							Math.sin(t) * Math.sin(s) * r , 
							Math.cos(s) * r );
		} else
		if(i == 3){
			//Cylin1 (c)
					double s = Math.PI * 0.5 - Math.PI * y; 
					double t = 2 * Math.PI * x; 
					
					double r = Math.cos(t*.5)*Math.sin(s*3)+1;
					
					return new Vec3D(
							r*Math.cos(s) *2,
							r*Math.sin(s) *2,
							2*t);
		} else
		if(i == 4){
			//Cylin2 (c)
					double s = Math.PI * 0.5 - Math.PI * y *2;
					double t = 2 * Math.PI * x;
					
					double r = (1+Math.max(Math.sin(t),0))*0.5*t;
					double z = 3-t;
					
					return new Vec3D(
							t*Math.cos(s),
							t*Math.sin(s),
							2*Math.sin(t));
		} else {
			//Sphere (k)
					double s = Math.PI * 0.5 - Math.PI * y; // zenit
					double t = 2* Math.PI * x; // azimiut
					double r = 20;
					
					return new Vec3D(
							Math.cos(t) * Math.cos(s) * r,
							Math.sin(t) * Math.cos(s) * r, 
							Math.sin(s) * r);
		}
	}

}
