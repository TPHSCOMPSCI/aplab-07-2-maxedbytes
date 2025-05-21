import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;



public class Stenography {

public static void main(String[] args) {
Picture beach = new Picture("beach.jpg");
Picture robot = new Picture("robot.jpg");

Picture hidden = hidePicture(beach, robot, 0, 0);
hidden.explore();

Picture revealed = revealPicture(hidden);
revealed.explore();

// Picture arch = new Picture("arch.jpg");
// Picture koala = new Picture("koala.jpg") ;
// Picture robot1 = new Picture("robot.jpg");
// Picture arch2 = hidePicture(arch, robot1, 65, 102);
// ArrayList<Point> pointList = findDifferences(arch, arch2);
// System.out.println("PointList after comparing two identical pictures " + "has a size of " + pointList.size());
// pointList = findDifferences(arch, koala);
// System.out.println("PointList after comparing two different sized pictures " + "has a size of " + pointList.size());
// pointList = findDifferences(arch, arch2);
// System.out.println("Pointlist after hiding a picture has a size of " + pointList.size());
// arch.show();
// arch2.show();

Picture hall = new Picture("femaleLionAndHall.jpg");
Picture robot2 = new Picture("robot.jpg");
Picture flower2 = new Picture("flower1.jpg");
// hide pictures
Picture hall2 = hidePicture(hall, robot2, 50, 300);
Picture hall3 = hidePicture(hall2, flower2, 115, 275);
hall3.explore();
if(!isSame(hall, hall3))
{
Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3));
hall4.show();
Picture unhiddenHall3 = revealPicture(hall3);
unhiddenHall3.show();
}

System.out.println("Can hide robot in beach: " + canHide(beach, robot));
System.out.println("Pictures are the same: " + isSame(hidden, revealed));

String secret = "What the sigma";

hideText(beach, secret);
String revealedText = revealText(beach);
System.out.println("Revealed text: " + revealedText);

if (encodeString(secret).size() <= beach.getHeight() * beach.getWidth()) {
hideText(beach, secret);
System.out.println("Text hidden successfully!");
revealedText = revealText(beach);
} else {
System.out.println("Text could not be hidden: image too small.");
}
}


public static void clearLow(Pixel p) {
p.setRed(p.getRed() / 4 * 4);
p.setGreen(p.getGreen() / 4 * 4);
p.setBlue(p.getBlue() / 4 * 4);
}

public static Picture testClearLow(Picture pic) {
Picture copy = new Picture(pic);
for (Pixel p : copy.getPixels()) {
clearLow(p);
}
return copy;
}

public static void setLow(Pixel p, Pixel secretPixel) {
clearLow(p);
p.setRed(p.getRed() + secretPixel.getRed() / 64);
p.setGreen(p.getGreen() + secretPixel.getGreen() / 64);
p.setBlue(p.getBlue() + secretPixel.getBlue() / 64);
}

public static Picture hidePicture(Picture source, Picture secret, int startRow, int startCol) {
Picture copy = new Picture(source);
Pixel[][] src = copy.getPixels2D();
Pixel[][] sec = secret.getPixels2D();

for (int r = 0; r < sec.length && r + startRow < src.length; r++) {
for (int c = 0; c < sec[0].length && c + startCol < src[0].length; c++) {
setLow(src[r + startRow][c + startCol], sec[r][c]);
}
}

return copy;
}

public static Picture revealPicture(Picture hidden) {
Picture revealed = new Picture(hidden);
Pixel[][] pixels = hidden.getPixels2D();
Pixel[][] rev = revealed.getPixels2D();

for (int r = 0; r < pixels.length; r++) {
for (int c = 0; c < pixels[0].length; c++) {
int red = (pixels[r][c].getRed() % 4) * 64;
int green = (pixels[r][c].getGreen() % 4) * 64;
int blue = (pixels[r][c].getBlue() % 4) * 64;
rev[r][c].setColor(new Color(red, green, blue));
}
}

return revealed;
}

public static boolean canHide(Picture source, Picture secret) {
return source.getWidth() >= secret.getWidth() &&
source.getHeight() >= secret.getHeight();
}

public static boolean isSame(Picture pic1, Picture pic2) {
if (pic1.getWidth() != pic2.getWidth() || pic1.getHeight() != pic2.getHeight()) {
return false;
}

Pixel[][] p1 = pic1.getPixels2D();
Pixel[][] p2 = pic2.getPixels2D();

for (int r = 0; r < p1.length; r++) {
for (int c = 0; c < p1[0].length; c++) {
if (!p1[r][c].getColor().equals(p2[r][c].getColor())) {
return false;
}
}
}

return true;
}

public static ArrayList<Point> findDifferences(Picture pic1, Picture pic2) {
ArrayList<Point> result = new ArrayList<Point>();
Pixel[][] onePix = pic1.getPixels2D();
Pixel[][] twoPix = pic2.getPixels2D();

for (int row = 0; row < onePix.length; row++) {
for (int col = 0; col < onePix[0].length; col++) {
if (!onePix[row][col].getColor().equals(twoPix[row][col].getColor())) {
result.add(new Point(col, row));
}
}
}
return result;
}

public static Picture showDifferentArea(Picture original, ArrayList<Point> differences) {
Picture copy = new Picture(original);

if (differences == null || differences.isEmpty()) {
return copy;
}

int minX = (int) differences.get(0).getX();
int minY = (int) differences.get(0).getY();
int maxX = minX;
int maxY = minY;

for (Point p : differences) {
int x = (int) p.getX();
int y = (int) p.getY();
if (x < minX) minX = x;
if (y < minY) minY = y;
if (x > maxX) maxX = x;
if (y > maxY) maxY = y;
}

Color red = Color.RED;

for (int x = minX; x <= maxX; x++) {
copy.getPixel(x, minY).setColor(red);
copy.getPixel(x, maxY).setColor(red);
}

for (int y = minY; y <= maxY; y++) {
copy.getPixel(minX, y).setColor(red);
copy.getPixel(maxX, y).setColor(red);
}

return copy;
}

public static ArrayList<Integer> encodeString(String s)
{
s = s.toUpperCase();
String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
ArrayList<Integer> result = new ArrayList<Integer>();
for (int i = 0; i < s.length(); i++)
{
if (s.substring(i,i+1).equals(" "))
{
result.add(27);
}
else
{
result.add(alpha.indexOf(s.substring(i,i+1))+1);
}
}
result.add(0);
return result;
}

public static String decodeString(ArrayList<Integer> codes)
{
String result="";
String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
for (int i=0; i < codes.size(); i++)
{
if (codes.get(i) == 27)
{
result = result + " ";
}
else
{
result = result + alpha.substring(codes.get(i)-1,codes.get(i));
}
}
return result;
}

private static int[] getBitPairs(int num)
{
int[] bits = new int[3];
int code = num;
for (int i = 0; i < 3; i++)
{
bits[i] = code % 4;
code = code / 4;
}
return bits;
}

public static void hideText(Picture source, String msg) {
ArrayList<Integer> codes = encodeString(msg); // Includes stop marker 0
Pixel[][] pixels = source.getPixels2D();

if (codes.size() > pixels.length * pixels[0].length) {
System.out.println("Error: Not enough space in the picture to hide the message.");
return;
}

int index = 0;
for (int row = 0; row < pixels.length; row++) {
for (int col = 0; col < pixels[0].length; col++) {
if (index >= codes.size()) return;

Pixel p = pixels[row][col];
int[] bits = getBitPairs(codes.get(index));

int red = p.getRed() / 4 * 4 + bits[0];
int green = p.getGreen() / 4 * 4 + bits[1];
int blue = p.getBlue() / 4 * 4 + bits[2];

p.setRed(red);
p.setGreen(green);
p.setBlue(blue);

index++;
}
}
}

public static String revealText(Picture source) {
Pixel[][] pixels = source.getPixels2D();
ArrayList<Integer> codes = new ArrayList<Integer>();

for (int row = 0; row < pixels.length; row++) {
for (int col = 0; col < pixels[0].length; col++) {
Pixel p = pixels[row][col];

int redBits = p.getRed() % 4;
int greenBits = p.getGreen() % 4;
int blueBits = p.getBlue() % 4;

int code = blueBits * 16 + greenBits * 4 + redBits;

if (code == 0) {
return decodeString(codes);
}
codes.add(code);
}
}

return decodeString(codes);
}
}
