package math;

public class Vector3D {
	public double x, y, z;
	public Vector3D(){
		x = y = z = 0;
	}
	public Vector3D(Vector3D v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		
	}
	public Vector3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3D crossProduct(Vector3D v){
		Vector3D result = new Vector3D(0, 0, 0);
		result.x = this.y * v.z - this.z * v.y;
		result.y = this.z * v.x - this.x * v.z;
		result.z = this.x * v.y - this.y * v.x;
		return result;
	}
	public void normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }
	public void add(Vector3D v){
		this.x = this.x + v.x;
		this.y = this.y + v.y;
		this.z = this.z + v.z;
		
	}
	public static Vector3D add(Vector3D u, Vector3D v){
		Vector3D r = new Vector3D(0, 0, 0);
		r.x = u.x + v.x;
		r.y = u.y + v.y;
		r.z = u.z + v.z;
		return r;
	}
	public void sub(Vector3D v){
		this.x = this.x - v.x;
		this.y = this.y - v.y;
		this.z = this.z - v.z;
		
	}
	public Vector3D scaleMul(double d) {
		Vector3D result = new Vector3D(0, 0, 0);
        result.x = d * this.x;
        result.y = d * this.y;
        result.z = d * this.z;
        return result;
    }
	public Vector3D mul(Vector3D v){
		Vector3D result = new Vector3D();
		result.x = v.x * this.x;
		result.y = v.y * this.y;
		result.z = v.z * this.z;
		return result;
	}
	
	public void set(Vector3D v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		
	}
	public double distance(Vector3D u) {
        return (double) Math.sqrt((this.x - u.x) * (this.x - u.x) +
                				  (this.y - u.y) * (this.y - u.y) +
                				  (this.z - u.z) * (this.z - u.z));
    }
	public double length(){
		return Math.sqrt(x * x + y * y + z * z);
	}
	@Override
	public String toString(){
		String description = "("+this.x+", "+this.y+", "+this.z+")";
		return description;
	}
}
