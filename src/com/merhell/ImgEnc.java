package com.merhell;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImgEnc {


	public int[] privatekey() {
		int sum,w[],q,r,rinv, i = 0;
		w = new int[10];
		Random rand = new Random();
		sum = 0;
		while (i < 8) {
			w[i] = rand.nextInt(sum + 3);
			if (w[i] > sum) {
				sum += w[i++];
				System.out.print(w[i - 1] + " ");
			}
		}
		System.out.println("\nSum:(sigma w)=" + sum);
		// q and r generation
		q = r = 0;
		while (q < sum)
			q = rand.nextInt(1000);
		// checking for co-prime
		while (r == 0) {
			r = rand.nextInt(q);
			for (i = 2; i <= r && (r % i != 0 || q % i != 0); i++)
				;
			if (i > r)
				break;
			else
				r = 0;
		}
		System.out.println("q=" + q + "  r=" + r);
		w[8] = q;
		w[9] = r;
		return w;
	}

	public int[] publickey(int []w,int q,int r) {
		int beta[] = new int[10];
		for (int i = 0; i < 8; i++) {
			beta[i] = (w[i] * r) % q;
			System.out.print(beta[i] + "  ");
		}
		return beta;
	}

	public BufferedImage[] encryption(BufferedImage image, int[] beta) {

		try {
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage img[] = new BufferedImage[2];
			img[0] = new BufferedImage(height, width,
					BufferedImage.TYPE_INT_ARGB);
			img[1] = new BufferedImage(height, width,
					BufferedImage.TYPE_INT_RGB);
			Color c_original, c_duplicate, c_code;
			for (int i = (height-1); i >=0; i--) {
				for (int j = (width-1); j >=0; j--) {
					c_original = new Color(image.getRGB(j, i));
					int red = c_original.getRed();
					int green = c_original.getGreen();
					int blue = c_original.getBlue();
					String redstring = "";
					String greenstring = "";
					String bluestring = "";
					for (int k = 0; k < 8; k++) {
						redstring += red % 2;
						red = red / 2;
						greenstring += green % 2;
						green = green / 2;
						bluestring += blue % 2;
						blue = blue / 2;
					}
					redstring = new StringBuffer(redstring).reverse()
							.toString();
					greenstring = new StringBuffer(greenstring).reverse()
							.toString();
					bluestring = new StringBuffer(bluestring).reverse()
							.toString();
					int ered = 0, egreen = 0, eblue = 0;
					int n = 8;
					while (--n >= 0) {
						if (redstring.charAt(n) == '1')
							ered += beta[n];
						if (greenstring.charAt(n) == '1')
							egreen += beta[n];
						if (bluestring.charAt(n) == '1')
							eblue += beta[n];
					}
					c_code = new Color(Math.abs(ered / 256),
							Math.abs(egreen / 256), Math.abs(eblue / 256),
							Math.abs(5));
					c_duplicate = new Color(Math.abs((ered) % 256),
							Math.abs((egreen) % 256), Math.abs((eblue) % 256));
					img[0].setRGB(i, j, c_code.getRGB());
					img[1].setRGB(i, j, c_duplicate.getRGB());
				}
			}
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem !!");
		}
		return null;
	}

	public BufferedImage decryption(int p[], String fname) {
		BufferedImage image, code;
		int q,r,rinv;
		int width;
		int height;
		BufferedImage img;
		Color c_original, c_duplicate, c_code;
		q = p[8];
		r = p[9];
		Random rand = new Random();
		rinv = rand.nextInt(1000);
		System.out.println("q : " + q + "\nr : " + r + "\nrinv : " + rinv);
		while ((r * rinv) % q != 1 && rinv != 0)
			rinv = rand.nextInt(1000);
		try {
			File input = new File(
					"E:\\Java EE Eclipse\\MyProjects\\FinalProject\\encrypted_images\\image_"
							+ fname + ".png");
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
			System.out.println("width :" + width + "\nheight :" + height);
			img = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
			File image1 = new File(
					"E:\\Java EE Eclipse\\MyProjects\\FinalProject\\encrypted_images\\code_"
							+ fname + ".png");
			code = ImageIO.read(image1);
			for (int i = (height-1); i >=0; i--) {
				for (int j = (width-1); j >=0; j--) {
					c_original = new Color(image.getRGB(j, i));
					c_code = new Color(code.getRGB(j, i));
					int red = c_original.getRed();
					red = red + 256 * c_code.getRed();
					int green = c_original.getGreen();
					green = green + 256 * c_code.getGreen();
					int blue = c_original.getBlue();
					blue = blue + 256 * c_code.getBlue();
					int dred = (red * rinv) % q;
					int dgreen = (green * rinv) % q;
					int dblue = (blue * rinv) % q;
					String x = "00000000";
					char[] xChars = x.toCharArray();
					String y = "00000000";
					char[] yChars = y.toCharArray();
					String z = "00000000";
					char[] zChars = z.toCharArray();
					xChars = check(dred, xChars, p);
					x = String.valueOf(xChars);
					yChars = check(dgreen, yChars, p);
					y = String.valueOf(yChars);
					zChars = check(dblue, zChars, p);
					z = String.valueOf(zChars);
					x = new StringBuffer(x).reverse().toString();
					y = new StringBuffer(y).reverse().toString();
					z = new StringBuffer(z).reverse().toString();
					int count = 7, xd, yd, zd;
					xd = yd = zd = 0;
					while (count >= 0) {
						if (x.charAt(count) == '1')
							xd = (int) (xd + Math.pow(2, count));
						if (y.charAt(count) == '1')
							yd = (int) (yd + Math.pow(2, count));
						if (z.charAt(count) == '1')
							zd = (int) (zd + Math.pow(2, count));
						count--;
					}
					c_duplicate = new Color(Math.abs(xd), Math.abs(yd),
							Math.abs(zd));
					img.setRGB(i, j, c_duplicate.getRGB());
				}
			}
			System.out.println("Image Created");
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem !!");
			return null;
		}
	}

	public char[] check(int dec, char[] Chars, int[] p) {
		while (dec > 0) {
			int count = 0;
			while (dec >= p[count] && count < 8) {
				count++;
			}
			count--;
			dec = dec - p[count];
			Chars[count] = '1';
		}
		return Chars;
	}
}